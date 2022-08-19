# Tools for dealing with macros in wrapped code

# TODO: remove this dependency
let stringtools = (import stringtools)

fn typename->Ctypename (sym)
    let T =
        try
            ('@ (globals) (sym as Symbol))
        else
            sym
    if (('typeof T) == type)
        T as:= type
        if (T < integer)
            signedness := (? ('signed? T) "signed" "unsigned")
            let _type =
                switch ('bitcount T)
                case 8
                    "char"
                case 16
                    "short int"
                case 32
                    "int"
                case 64
                    "long int"
                default
                    error "unsupported type translation"
            (.. signedness " " _type)
        elseif (T < real)
            let _type =
                switch ('bitcount T)
                case 32
                    "float"
                case 64
                    "double"
                default
                    error "unsupported type translation"
            _type
        else
            error "unsupported type translation"
    else
        tostring (T as Symbol)

let macro-wrapper-prefix = "scopes_macro_wrapper__"
let macro-wrapper-regexp = (.. "^" macro-wrapper-prefix)
let constant-wrapper-prefix = "scopes_constant_wrapper__"
let constant-wrapper-regexp = (.. "^" constant-wrapper-prefix)

fn gen-C-arglist (args)
    argcount := ('argcount args)
    if (argcount == 0)
        ""
    fold (result = "") for i arg in (enumerate ('args args))
        arg as:= string
        last? := (i == (argcount - 1))
        if last?
            result .. arg
        else
            .. result arg ", "

# runtime dependant
fn gen-macro-wrapper-fn (macro args)
    argcount := ('argcount args)
    using import itertools
    let args =
        ->> ('args args)
            map typename->Ctypename
            Value.arglist-sink argcount
    let dummy-values =
        ->> ('args args)
            map
                inline (argT)
                    stringtools.interpolate "(${argT as string}){0}"
            Value.arglist-sink argcount

    let fn-args =
        ->> (enumerate ('args args))
            map
                inline (i argT)
                    stringtools.interpolate "${argT as string} arg${i}"
            Value.arglist-sink argcount

    let forwarded =
        ->> (range argcount)
            map
                inline (i)
                    "arg" .. (tostring i)
            Value.arglist-sink argcount

    stringtools.interpolate
        """"typeof(${macro}(${gen-C-arglist dummy-values}))
            ${macro-wrapper-prefix}${macro} (${gen-C-arglist fn-args}) {
                return ${macro}(${gen-C-arglist forwarded});
            }

fn gen-constant-wrapper-fn (macro)
    stringtools.interpolate
        """"typeof(${macro}) ${constant-wrapper-prefix}${macro} () {
                return ${macro};
            }

sugar foreign (args...)
    let args result include-args code =
        loop (args result include-args code = args... '() '() "")
            if (empty? args)
                break args result include-args code
            sugar-match args
            case ((header as string) rest...)
                # the \n at the end is to signal to `include` that this is to be compiled
                # NOTE: enveloping the includestr in quotes here is a choice!
                # I can't guarantee it'll always be the correct one, but I haven't seen a good
                # argument that you can't just always use quotes instead of angled brackets.
                incstr := (.. "#include \"" header "\"\n")
                let code =
                    .. incstr code # include string must go first!
                _ rest... result include-args code
            case (('with-constants defines...) rest...)
                let at next = (decons defines...)
                let wrappers =
                    loop (_define rest wrapper-code = at next "")
                        code := (wrapper-code .. (gen-constant-wrapper-fn (_define as Symbol)))
                        if (empty? rest)
                            break code
                        let at next = (decons rest)
                        _ at next code
                # wrappers have to go at the 'bottom' to preserve the includes
                code := (code .. wrappers)
                _ rest... result include-args code
            case (('with-macros macros...) rest...)
                let at next = (decons macros...)
                let wrappers =
                    loop (_define rest wrapper-code = at next "")
                        let code =
                            if (('typeof _define) == list)
                                using import itertools
                                macro := (_define as list)
                                let name args = (decons macro)
                                let arglist =
                                    ->> args (Value.arglist-sink (countof args))

                                wrapper-code .. (gen-macro-wrapper-fn (name as Symbol) arglist)
                            else
                                let arglist = (sc_argument_list_new 0 null)
                                wrapper-code .. (gen-macro-wrapper-fn (_define as Symbol) arglist)

                        if (empty? rest)
                            break code

                        let at next = (decons rest)
                        _ at next code

                _ rest... result include-args (code .. wrappers)
            default
                let at next = (decons args)
                include-args := (cons at include-args)
                _ (next as list) result include-args code

    include-args := (cons code include-args)
    _
        qq [(cons 'include include-args)]
        next-expr