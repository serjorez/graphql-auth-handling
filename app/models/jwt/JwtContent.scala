package models.jwt

import play.api.libs.json._

case class JwtContent(id: Long)

object JwtContent {
  implicit val jwtContentFormat = Json.format[JwtContent]
}