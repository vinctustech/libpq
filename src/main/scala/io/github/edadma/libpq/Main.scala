package io.github.edadma.libpq

@main def run(): Unit =
  println(s"version of libpq: $libVersion")

//val conn = connectdb("dbname=postgres user=postgres password=docker host=localhost")
//
//if conn.status == ConnStatus.BAD then
//  println(s"connection failed ${conn.errorMessage}")
//  conn.finish()
//
