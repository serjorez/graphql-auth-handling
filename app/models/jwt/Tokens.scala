package models.jwt

/**
  * An entity that contains access and refresh token.
  *
  * @param accessToken  an access token
  * @param refreshToken a refresh token
  */
case class Tokens(accessToken: String, refreshToken: String)