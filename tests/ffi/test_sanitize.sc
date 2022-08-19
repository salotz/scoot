""""Tests for the 'ffi' module

using import testing

print "----------------------------------------"
print "FFI"
print "----------------------------------------"

let ffi = (import scoot.ffi.sanitize)

# path to test data
let data_path = (.. module-dir "/" "_sanitize")

let simple =
    include
        "simple.h"
        options
            .. "-I" data_path

# cleanup and test
let sanitized_simple =
    ..
        (ffi.sanitize-bindings simple.define "^SPL_")
        (ffi.sanitize-bindings simple.typedef "^Spl")
        (ffi.sanitize-bindings simple.struct "^Spl")
        (ffi.sanitize-bindings simple.extern "^spl_")
        (ffi.sanitize-bindings simple.const "^spl_")

run-stage;

# test that the symbols were renamed correctly
print sanitized_simple.JACK

;
