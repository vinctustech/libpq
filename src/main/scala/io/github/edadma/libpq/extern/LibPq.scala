package io.github.edadma.libpq.extern

import scala.scalanative.unsafe._

@link("pq")
@extern
object LibPq:

  type PGconn = CStruct0
  type PGconnp = Ptr[PGconn]
  type PGresult = CStruct0
  type PGresultp = Ptr[PGresult]
  type PGcancel = CStruct0

  type ExecStatusType = CInt
  type ConnStatusType = CInt

  def PQconnectdb(conninfo: CString): PGconnp = extern
  def PQfinish(conn: PGconnp): Unit = extern
  def PQexec(conn: PGconnp, query: CString): PGresultp = extern

  /* Accessor functions for PGconn objects */
  def PQstatus(conn: PGconnp): ConnStatusType = extern
  def PQerrorMessage(conn: PGconnp): CString = extern
  def PQserverVersion(conn: PGconnp): CInt = extern

  /* Accessor functions for PGresult objects */
  def PQresultStatus(res: PGresultp): ExecStatusType = extern
  def PQntuples(res: PGresultp): CInt = extern
  def PQnfields(res: PGresultp): CInt = extern
  def PQfname(res: PGresultp, field_num: CInt): CString = extern
  def PQgetvalue(res: PGresultp, tup_num: CInt, field_num: CInt): CString = extern

  /* Delete a PGresult */
  def PQclear(res: PGresultp): Unit = extern

  /* Get the version of the libpq library in use */
  def PQlibVersion: CInt = extern
