package models.errors

import sangria.execution.UserFacingError

//todo add docs
case class Unauthenticated(msg: String) extends Exception(msg) with UserFacingError {
  override def getMessage(): String = msg
}