package services

import models.jwt.{JwtContent, Tokens}

import scala.util.Try

trait JwtAuthService {

  def createAccessToken(content: JwtContent): String

  def createRefreshToken(content: JwtContent, secret: String): String

  def createTokens(content: JwtContent, secret: String): Tokens

  def decodeContent(token: String): Try[JwtContent]

  def decodeAccessToken(token: String): Try[JwtContent]

  def decodeRefreshToken(token: String, secret: String): Try[JwtContent]
}