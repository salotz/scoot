# Original Author: Westerbly (radgeRayden) Snaydley
# Date: 2021-01-21

inline sanitize-bindings (scope pattern)
    # Scope objects are immutable, so we have to build the new one
      iteratively.
    fold (scope = scope) for k v in scope
        # the scope generator returns symbols as boxed Values, so we
          need to unbox to Symbol first.
        let name = (k as Symbol as string)
        # the 'match? method in string uses the C++ regex engine.
        let match? start end = ('match? pattern name)
        if match?
            # here we make the assumption that we are always removing
              a prefix, so rslice suffices.
            let new-name = (rslice name end)
            'bind scope (Symbol new-name) v
        else
            # return the unmodified Scope.
            scope

do
    let sanitize-bindings
    locals;
