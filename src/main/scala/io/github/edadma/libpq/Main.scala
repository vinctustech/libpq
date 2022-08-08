package io.github.edadma.libpq

@main def run(): Unit =
//  println(s"version of libpq: $libVersion")

//  val conn = connectdb("dbname=postgres user=postgres password=docker host=localhost")
//
//  if conn.status == ConnStatus.BAD then
//    println(s"connection failed ${conn.errorMessage}")
//    conn.finish()
//    sys.exit(1)
//
//  println(s"server version: ${conn.serverVersion}")
//  conn.finish()

//  val conn = connectdb("dbname=postgres user=postgres password=docker host=localhost")
//
//  if conn.status == ConnStatus.BAD then
//    println(s"connection failed ${conn.errorMessage}")
//    conn.finish()
//    sys.exit(1)
//
//  def exec(query: String): Unit =
//    val res = conn.exec(query)
//
//    if res.status != ExecStatus.COMMAND_OK then
//      println(conn.errorMessage)
//      res.clear()
//      conn.finish()
//      sys.exit(1)
//
//    res.clear()
//
//  exec("DROP TABLE IF EXISTS Cars")
//  exec("""
//      |CREATE TABLE Cars (
//      | Id INTEGER PRIMARY KEY,
//      | Name VARCHAR(20),
//      | Price INT
//      |)
//      |""".stripMargin)
//  exec("INSERT INTO Cars VALUES(1,'Audi',52642)")
//  exec("INSERT INTO Cars VALUES(2,'Mercedes',57127)")
//  conn.finish()

  val conn = connectdb("dbname=postgres user=postgres password=docker host=localhost")

  if conn.status == ConnStatus.BAD then
    println(s"connection failed ${conn.errorMessage}")
    conn.finish()
    sys.exit(1)

  def exec(query: String): Unit =
    val res = conn.exec(query)

    if res.status != ExecStatus.COMMAND_OK then
      println(conn.errorMessage)
      res.clear()
      conn.finish()
      sys.exit(1)

    res.clear()

  exec("DROP TABLE IF EXISTS Cars")
  exec("""
      |CREATE TABLE Cars (
      | Id INTEGER PRIMARY KEY, 
      | Name VARCHAR(20), 
      | Price INT
      |)
      |""".stripMargin)
  exec("INSERT INTO Cars VALUES(1,'Audi',52642)")
  exec("INSERT INTO Cars VALUES(2,'Mercedes',57127)")
  conn.finish()
