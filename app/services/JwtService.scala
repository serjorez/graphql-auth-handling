package services

import models.jwt.{JwtContent, Tokens}

import scala.util.Try

/** Provide the main logic around the manipulation with JWT.
  *
  * @define content representation of a content of a token claim
  * @define token   a JSON Web Token as a Base64 url-safe encoded String which can be used inside an HTTP header
  * @define secret  an encoded String which can be used in a validation step
  */
trait JwtService {

  /** Create access token (JWT with a short lifetime).
    * Access token's secret should contain only server secret (that should be stored in implementation).
    *
    * @return $token
    * @param content $content
    */
  def createAccessToken(content: JwtContent): String

  /** Create refresh token (JWT with a long lifetime).
    * Refresh token's secret should contain server secret + any user-specific information.
    *
    * @return $token
    * @param content $content
    * @param secret  $secret
    */
  def createRefreshToken(content: JwtContent, secret: String): String

  /** Create access and refresh tokens.
    * Access token's secret should contain only server secret (that should be stored in implementation).
    * Refresh token's secret should contain server secret + any user-specific information.
    *
    * @return the access and refresh token
    * @param content $content
    * @param secret  $secret
    */
  def createTokens(content: JwtContent, secret: String): Tokens

  /** Decode token content without signature validation.
    *
    * @return $content
    * @param token $token
    */
  def decodeContent(token: String): Try[JwtContent]

  /** Decode access token (JWT with a short lifetime).
    * Access token's secret should contain only server secret (that should be stored in implementation).
    *
    * @return $content
    * @param token $token
    */
  def decodeAccessToken(token: String): Try[JwtContent]

  /** Decode refresh token (JWT with a long lifetime).
    * Refresh token's secret should contain server secret + any user-specific information.
    *
    * @return $content
    * @param token  $token
    * @param secret $secret
    */
  def decodeRefreshToken(token: String, secret: String): Try[JwtContent]
}