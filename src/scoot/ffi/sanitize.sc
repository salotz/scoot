""""Tools to clean up symbols in wrapped libraries.

    This module addresses common stylistic issues when wrapping and
    integrating code from C. This includes things like removing symbol
    prefixes (e.g. `GLFW_THING` to just `THING`). In Scopes we don't need
    to overload symbols with scope information since there are scopes to
    do this.

# Original Author: Westerbly (radgeRayden) Snaydley
# Date: 2021-01-21

inline... sanitize-bindings
case (
    scope : Scope,
    pattern : string,
    keep-cleaned : bool = false)
    """"Given a scope apply a pattern to remove from all symbols.

        Parameters
            scope
                The scope you want to transform.

            pattern
                The pattern you want to remove, C++ regex style using
                the `match?` function.

            keep-cleaned : (optional)
                If true the matched symbols will be kept in the scope,
                otherwise they will be removed.

        Returns
            sanitized-scope
                The scope with transformed symbols.

    let init_scope = scope

    if (not keep-cleaned)
        let init_scope = (Scope)

    # Scope objects are immutable, so we have to build the new one
      iteratively.
    fold (scope = init_scope) for k v in scope
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
