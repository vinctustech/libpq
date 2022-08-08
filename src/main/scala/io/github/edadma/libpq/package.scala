package io.github.edadma

import scala.scalanative.unsafe._
import scala.scalanative.unsigned._

package object libpq:

  import extern.{LibPq => lib}

  class ConnStatusType(val value: lib.ConnStatusType) extends AnyVal

  object ConnStatusType:
    final val CONNECTION_OK = new ConnStatusType(0)
    final val CONNECTION_BAD = new ConnStatusType(1)
    final val CONNECTION_STARTED = new ConnStatusType(2)
    final val CONNECTION_MADE = new ConnStatusType(3)
    final val CONNECTION_AWAITING_RESPONSE = new ConnStatusType(4)
    final val CONNECTION_AUTH_OK = new ConnStatusType(5)
    final val CONNECTION_SETENV = new ConnStatusType(6)
    final val CONNECTION_SSL_STARTUP = new ConnStatusType(7)
    final val CONNECTION_NEEDED = new ConnStatusType(8)
    final val CONNECTION_CHECK_WRITABLE = new ConnStatusType(9)
    final val CONNECTION_CONSUME = new ConnStatusType(10)
    final val CONNECTION_GSS_STARTUP = new ConnStatusType(11)
    final val CONNECTION_CHECK_TARGET = new ConnStatusType(12)
    final val CONNECTION_CHECK_STANDBY = new ConnStatusType(13)

  class ExecStatusType(val value: lib.ExecStatusType) extends AnyVal

  object ExecStatusType:
    final val PGRES_EMPTY_QUERY = new ExecStatusType(0)
    final val PGRES_COMMAND_OK = new ExecStatusType(1)
    final val PGRES_TUPLES_OK = new ExecStatusType(2)
    final val PGRES_COPY_OUT = new ExecStatusType(3)
    final val PGRES_COPY_IN = new ExecStatusType(4)
    final val PGRES_BAD_RESPONSE = new ExecStatusType(5)
    final val PGRES_NONFATAL_ERROR = new ExecStatusType(6)
    final val PGRES_FATAL_ERROR = new ExecStatusType(7)
    final val PGRES_COPY_BOTH = new ExecStatusType(8)
    final val PGRES_SINGLE_TUPLE = new ExecStatusType(9)
    final val PGRES_PIPELINE_SYNC = new ExecStatusType(10)
    final val PGRES_PIPELINE_ABORTED = new ExecStatusType(11)

  implicit class Connection private[libpq] (val conn: lib.PGconnp) extends AnyVal:
    def status: ConnStatusType = lib.PQstatus(conn)

    def finish(): Unit = lib.PQfinish(conn)

    def exec(query: CString): lib.PGresultp = Zone(implicit z => lib.PQexec(conn, toCString(query)))
  end Connection

  implicit class Result private[libpq] (val result: lib.PGresultp) extends AnyVal:
    def resultStatus: ExecStatusType = lib.PQresultStatus(result)

    def ntuples: Int = lib.PQntuples(result)

    def nfields: Int = lib.PQnfields(result)

    def fname(field_num: Int): String = fromCString(lib.PQfname(result, field_num))

    def getvalue(tup_num: CInt, field_num: CInt): CString = fromCString(lib.PQgetvalue(result, tup_num, field_num))

    def clear(): Unit = lib.PQclear(result)
  end Result

  def connectdb(conninfo: String): Connection = Zone { implicit z => lib.PQconnectdb(toCString(conninfo)) }
