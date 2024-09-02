package models

import play.api.http.Status

sealed abstract class DatabaseError(val ResponseStatus: Int, val reason: String)

object DatabaseError{
  final case class BadAPIResponse(getStatus: Int, dbMessage: String) extends DatabaseError(
    Status.INTERNAL_SERVER_ERROR, s"Bad response from upstream. Got status: $getStatus, and got reason: $dbMessage."
  )
}
