package io.github.edadma

import scala.scalanative.unsafe._
import scala.scalanative.unsigned._

package object libpq:

  import extern.{LibPq => lib}

  implicit class ConnStatus(val value: lib.ConnStatusType) extends AnyVal

  object ConnStatus:
    final val OK = new ConnStatus(0)
    final val BAD = new ConnStatus(1)
    final val STARTED = new ConnStatus(2)
    final val MADE = new ConnStatus(3)
    final val AWAITING_RESPONSE = new ConnStatus(4)
    final val AUTH_OK = new ConnStatus(5)
    final val SETENV = new ConnStatus(6)
    final val SSL_STARTUP = new ConnStatus(7)
    final val NEEDED = new ConnStatus(8)
    final val CHECK_WRITABLE = new ConnStatus(9)
    final val CONSUME = new ConnStatus(10)
    final val GSS_STARTUP = new ConnStatus(11)
    final val CHECK_TARGET = new ConnStatus(12)
    final val CHECK_STANDBY = new ConnStatus(13)

  implicit class ExecStatus(val value: lib.ExecStatusType) extends AnyVal

  object ExecStatus:
    final val EMPTY_QUERY = new ExecStatus(0)
    final val COMMAND_OK = new ExecStatus(1)
    final val TUPLES_OK = new ExecStatus(2)
    final val COPY_OUT = new ExecStatus(3)
    final val COPY_IN = new ExecStatus(4)
    final val BAD_RESPONSE = new ExecStatus(5)
    final val NONFATAL_ERROR = new ExecStatus(6)
    final val FATAL_ERROR = new ExecStatus(7)
    final val COPY_BOTH = new ExecStatus(8)
    final val SINGLE_TUPLE = new ExecStatus(9)
    final val PIPELINE_SYNC = new ExecStatus(10)
    final val PIPELINE_ABORTED = new ExecStatus(11)

  implicit class Connection private[libpq] (val conn: lib.PGconnp) extends AnyVal:
    def status: ConnStatus = lib.PQstatus(conn)

    def finish(): Unit = lib.PQfinish(conn)

    def exec(query: String): Result = Zone(implicit z => lib.PQexec(conn, toCString(query)))

    def errorMessage: String = fromCString(lib.PQerrorMessage(conn))
  end Connection

  implicit class Result private[libpq] (val result: lib.PGresultp) extends AnyVal:
    def status: ExecStatus = lib.PQresultStatus(result)

    def ntuples: Int = lib.PQntuples(result)

    def nfields: Int = lib.PQnfields(result)

    def fname(field_num: Int): String = fromCString(lib.PQfname(result, field_num))

    def getvalue(tup_num: Int, field_num: Int): String = fromCString(lib.PQgetvalue(result, tup_num, field_num))

    def clear(): Unit = lib.PQclear(result)
  end Result

  def connectdb(conninfo: String): Connection = Zone { implicit z => lib.PQconnectdb(toCString(conninfo)) }

  def libVersion: Int = lib.PQlibVersion
