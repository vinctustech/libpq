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

  implicit class Oid(val value: lib.Oid) extends AnyVal

  object Oid:
    final val BOOLOID = new Oid(16.toUInt)
    final val BYTEAOID = new Oid(17.toUInt)
    final val CHAROID = new Oid(18.toUInt)
    final val NAMEOID = new Oid(19.toUInt)
    final val INT8OID = new Oid(20.toUInt)
    final val INT2OID = new Oid(21.toUInt)
    final val INT2VECTOROID = new Oid(22.toUInt)
    final val INT4OID = new Oid(23.toUInt)
    final val REGPROCOID = new Oid(24.toUInt)
    final val TEXTOID = new Oid(25.toUInt)
    final val OIDOID = new Oid(26.toUInt)
    final val TIDOID = new Oid(27.toUInt)
    final val XIDOID = new Oid(28.toUInt)
    final val CIDOID = new Oid(29.toUInt)
    final val OIDVECTOROID = new Oid(30.toUInt)
    final val JSONOID = new Oid(114.toUInt)
    final val POINTOID = new Oid(600.toUInt)
    final val LSEGOID = new Oid(601.toUInt)
    final val PATHOID = new Oid(602.toUInt)
    final val BOXOID = new Oid(603.toUInt)
    final val POLYGONOID = new Oid(604.toUInt)
    final val LINEOID = new Oid(628.toUInt)
    final val FLOAT4OID = new Oid(700.toUInt)
    final val FLOAT8OID = new Oid(701.toUInt)
    final val ABSTIMEOID = new Oid(702.toUInt)
    final val RELTIMEOID = new Oid(703.toUInt)
    final val TINTERVALOID = new Oid(704.toUInt)
    final val UNKNOWNOID = new Oid(705.toUInt)
    final val CIRCLEOID = new Oid(718.toUInt)
    final val CASHOID = new Oid(790.toUInt)
    final val INETOID = new Oid(869.toUInt)
    final val CIDROID = new Oid(650.toUInt)
    final val BPCHAROID = new Oid(1042.toUInt)
    final val VARCHAROID = new Oid(1043.toUInt)
    final val DATEOID = new Oid(1082.toUInt)
    final val TIMEOID = new Oid(1083.toUInt)
    final val TIMESTAMPOID = new Oid(1114.toUInt)
    final val TIMESTAMPTZOID = new Oid(1184.toUInt)
    final val INTERVALOID = new Oid(1186.toUInt)
    final val TIMETZOID = new Oid(1266.toUInt)
    final val ZPBITOID = new Oid(1560.toUInt)
    final val VARBITOID = new Oid(1562.toUInt)
    final val NUMERICOID = new Oid(1700.toUInt)

  implicit class Connection(val conn: lib.PGconnp) extends AnyVal:
    def status: ConnStatus = lib.PQstatus(conn)

    def finish(): Unit = lib.PQfinish(conn)

    def exec(query: String): Result = Zone(implicit z => lib.PQexec(conn, toCString(query)))

    def errorMessage: String = fromCString(lib.PQerrorMessage(conn))

    def serverVersion: Int = lib.PQserverVersion(conn)

    def sendQuery(query: String): Boolean =
      Zone(implicit z => lib.PQsendQuery(conn, toCString(query))) != 0 // false if error

    def getResult: Result = lib.PQgetResult(conn)

    def consumeInput: Boolean = lib.PQconsumeInput(conn) != 0 // false if error

    def isBusy: Boolean = lib.PQisBusy(conn) != 0 // true if getResult would block

    def socket: Int = lib.PQsocket(conn)
  end Connection

  implicit class Result(val result: lib.PGresultp) extends AnyVal:
    def isNull: Boolean = result == null

    def status: ExecStatus = lib.PQresultStatus(result)

    def nTuples: Int = lib.PQntuples(result)

    def nFields: Int = lib.PQnfields(result)

    def fName(field_num: Int): String = fromCString(lib.PQfname(result, field_num))

    def getValue(tup_num: Int, field_num: Int): String = fromCString(lib.PQgetvalue(result, tup_num, field_num))

    def getIsNull(tup_num: Int, field_num: Int): Boolean = lib.PQgetisnull(result, tup_num, field_num) > 0

    def getLength(tup_num: Int, field_num: Int): Int = lib.PQgetlength(result, tup_num, field_num)

    def clear(): Unit = lib.PQclear(result)

    def fType(field_num: CInt): Oid = lib.PQftype(result, field_num)

    def fSize(field_num: CInt): Int = lib.PQfsize(result, field_num)
  end Result

  def connectDB(conninfo: String): Connection = Zone { implicit z => lib.PQconnectdb(toCString(conninfo)) }

  def libVersion: Int = lib.PQlibVersion
