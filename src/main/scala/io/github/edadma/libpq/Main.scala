//package io.github.edadma.libpq
//
//@main def run(): Unit =
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

//  val conn = connectdb("dbname=postgres user=postgres password=docker host=localhost")
//
//  if conn.status == ConnStatus.BAD then
//    println(s"connection failed ${conn.errorMessage}")
//    conn.finish()
//    sys.exit(1)
//
//  val res = conn.exec("SELECT VERSION()")
//
//  if res.status != ExecStatus.TUPLES_OK then
//    println("no data")
//    res.clear()
//    conn.finish()
//    sys.exit(1)
//
//  println(s"server version: ${res.getvalue(0, 0)}")
//  res.clear()
//  conn.finish()

//  val conn = connectdb("dbname=postgres user=postgres password=docker host=localhost")
//
//  if conn.status == ConnStatus.BAD then
//    println(s"connection failed ${conn.errorMessage}")
//    conn.finish()
//    sys.exit(1)
//
//  val res = conn.exec("SELECT * FROM Cars LIMIT 5")
//
//  if res.status != ExecStatus.TUPLES_OK then
//    println("no data")
//    res.clear()
//    conn.finish()
//    sys.exit(1)
//
//  val rows = res.ntuples
//
//  for i <- 0 until rows do println(s"${res.getvalue(i, 0)} ${res.getvalue(i, 1)} ${res.getvalue(i, 2)}")
//
//  res.clear()
//  conn.finish()

//  val conn = connectdb("dbname=postgres user=postgres password=docker host=localhost")
//
//  if conn.status == ConnStatus.BAD then
//    println(s"connection failed ${conn.errorMessage}")
//    conn.finish()
//    sys.exit(1)
//
//  val res = conn.exec("SELECT * FROM Cars")
//
//  if res.status != ExecStatus.TUPLES_OK then
//    println("no data")
//    res.clear()
//    conn.finish()
//    sys.exit(1)
//
//  val cols = res.nfields
//
//  println(s"there are $cols columns")
//  println("column names:")
//
//  for i <- 0 until cols do println(s"${res.fname(i)}")
//
//  res.clear()
//  conn.finish()
