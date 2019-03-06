package models.errors

import sangria.execution.UserFacingError

/**
  * Represents an exception object indicating that token is invalid.
  *
  * @param msg an exception message to show
  */
case class InvalidToken(msg: String) extends Exception with UserFacingError {
  override def getMessage: String = msg
}