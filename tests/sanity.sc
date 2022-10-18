""""Check that the template is working properly by using the special
    __sanity module.

using import testing

print "----------------------------------------"
print "sanity"
print "----------------------------------------"

let sanity = (import scoot.__sanity)

test
    ==
        (sanity.check-sanity)
        "Sanity Check Successful"
