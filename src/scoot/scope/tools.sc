""""Utilities for working with scopes.

fn... in-scope? (scope : Scope, sym : Symbol)
    """"Test whether a symbol is in a scope.

        Parameters
            scope
            sym

        Returns
            success

    returning (_: bool)

    local success = false

    for k v in scope
        if ((k as Symbol) == sym)
            success = true
            break;

    success

do
    let in-scope?
    locals;
