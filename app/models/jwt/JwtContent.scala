package models.jwt

import play.api.libs.json._

/**
  * An entity that is contained in the JWT token.
  *
  * @param id user identifier
  */
case class JwtContent(id: Long)

object JwtContent {
  implicit val jwtContentFormat: OFormat[JwtContent] = Json.format[JwtContent]
}