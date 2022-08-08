package io.github.edadma.libpq

@main def run(): Unit =
  val conn = connectdb("dbname=postgres user=postgres password=docker host=localhost")
