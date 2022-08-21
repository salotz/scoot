""""Tests for the 'scope' module

using import testing

print "----------------------------------------"
print "scope"
print "----------------------------------------"

let scopetools = (import scoot.scope.tools)

## set up some scopes for testing
vvv bind sc
do
    let
        x = 3
        y = 6
    locals;

run-stage;

test (scopetools.in-scope? sc 'x)
test (scopetools.in-scope? sc 'y)
test (not (scopetools.in-scope? sc 'z))
