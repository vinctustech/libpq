libpq
=====

*libpq* provides a [libpq](https://www.postgresql.org/docs/current/libpq.html) foreign function interface (FFI) for Scala Native. A Scala Native application can link (using [LLD](https://lld.llvm.org/)) with a C library but the C functions that it needs to use from that library have to be declared in a particular way. Additionally, a good FFI should provide another layer around the basic C function bindings that allows those functions to be used in a way that only requires knowledge of the Scala programming language and does not require any knowledge of C.

To use this library for application development, you only need to import from `io.github.edadma.libpq`, and not from the subpackage `extern` which provides the raw C library bindings for internal use only.

The following example program performes a database query to find out Bob's last name.
```scala
package io.github.edadma.libpq

@main def run(): Unit =
  val conn = connectDB("dbname=<database name> user=<user name> password=<user password> host=<hostname>")

  if conn.status == ConnStatus.BAD then
    println(s"connection failed ${conn.errorMessage}")
    conn.finish()
    sys.exit(1)

  val res = conn.exec("SELECT lastname FROM users WHERE email='bob@example.com'")

  if res.status != ExecStatus.TUPLES_OK then
    println("no data")
    res.clear()
    conn.finish()
    sys.exit(1)

  val rows = res.nTuples

  for i <- 0 until rows do println(s"${res.getValue(i, 0)}")

  res.clear()
  conn.finish()
```
