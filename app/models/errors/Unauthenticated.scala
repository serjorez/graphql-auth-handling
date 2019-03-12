package models.errors

import sangria.execution.UserFacingError

/**
  * Represents an exception object indicating that you are need to be authenticated to perform operation.
  *
  * @param msg an exception message to show
  */
case class Unauthenticated(msg: String) extends Exception(msg) with UserFacingError {
  override def getMessage(): String = msg
}