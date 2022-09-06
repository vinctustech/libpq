libpq
=====

*libpq* provides a [libpq](https://www.postgresql.org/docs/current/libpq.html) foreign function interface (FFI) for Scala Native. A Scala Native application can link (using [LLD](https://lld.llvm.org/)) with a C library but the C functions that it needs to use from that library have to be declared in a particular way. Additionally, a good FFI should provide another layer around the basic C function bindings that allows those functions to be used in a way that only requires knowledge of the Scala programming language and does not require any knowledge of C.

To use this library for application development, you only need to import from `io.github.edadma.libpq`, and not from the subpackage `extern` which provides the raw C library bindings for internal use only.
