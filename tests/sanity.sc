""""Check that the template is working properly by using the special
    __sanity module.

print "----------------------------------------"
print "FFI"
print "----------------------------------------"

using import testing

let sanity = (import scoot.__sanity)

test
    ==
        (sanity.check-sanity)
        "Sanity Check Successful"
