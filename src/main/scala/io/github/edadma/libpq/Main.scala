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

//  println(query("select * from cars"))
//
//import scala.collection.immutable.ArraySeq
//import scala.concurrent.Future
//import io.github.edadma.libpq.*
//
//import scala.annotation.tailrec
//import scala.collection.mutable.ArrayBuffer
//
//import scala.scalanative.posix.poll.{poll, struct_pollfd}
//import scala.scalanative.posix.pollEvents.POLLIN
//import scala.scalanative.posix.pollOps._
//import scala.scalanative.unsafe._
//import scala.scalanative.unsigned._
//
//def query(sql: String): PGResult =
//  val conn = connectdb("dbname=postgres user=postgres password=docker host=localhost")
//
//  def error(msg: String): Nothing =
//    conn.finish()
//    sys.error(msg)
//
//  if conn.status == ConnStatus.BAD then error(s"connectdb() failed: ${conn.errorMessage}")
//
//  println(s"connected: $conn")
//
//  val socket = conn.socket
//
//  if socket < 0 then error(s"bad socket: $socket: ${conn.errorMessage}")
//
//  val sendres = conn.sendQuery(sql)
//
//  if !sendres then error(s"sendQuery() failed: ${conn.errorMessage}")
//
//  val buf = new ArrayBuffer[ArraySeq[Any]]
//  var columns: ArraySeq[String] = null
//
//  val pollfd = stackalloc[struct_pollfd]()
//
//  pollfd.fd = socket
//  pollfd.events = POLLIN
//
//  println("polling")
//
//  val pollres = poll(pollfd, 1.toULong, 1000)
//
//  if pollres == 0 then error("timed out")
//  if pollres < 0 then error(s"polling error: $pollres")
//
//  println(s"poll result: $pollres")
//
//  if !conn.consumeInput then error(s"consumeInput() failed: ${conn.errorMessage}")
//
//  while conn.isBusy do if !conn.consumeInput then error(s"consumeInput() failed: ${conn.errorMessage}")
//
//  @tailrec
//  def results(): Unit =
//    println("getResult")
//
//    val res = conn.getResult
//
//    if !res.isNull then
//      val rows = res.ntuples
//      val cols = res.nfields
//
//      if columns == null then columns = (for i <- 0 until cols yield res.fname(i)) to ArraySeq
//      for i <- 0 until rows do buf += (for j <- 0 until cols yield res.getvalue(i, j)) to ArraySeq
//
//      res.clear()
//      results()
//
//  results()
//  conn.finish()
//  new PGResult(columns, buf to ArraySeq)
//
//class PGResult(val columns: ArraySeq[String], val data: ArraySeq[ArraySeq[Any]]):
//  override def toString: String =
//    columns.toString ++ "\n" ++ data.mkString("\n")
