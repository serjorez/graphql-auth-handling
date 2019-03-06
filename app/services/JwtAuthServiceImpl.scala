package services

import play.api.libs.json._
import models.jwt.JwtContent._
import com.google.inject.Inject
import config.JwtConfig
import models.errors.InvalidToken
import models.jwt.{JwtContent, Tokens}
import pdi.jwt.{Jwt, JwtClaim, JwtOptions}
import pdi.jwt.algorithms.JwtHmacAlgorithm
import pdi.jwt.exceptions.JwtExpirationException

import scala.util.Try

class JwtAuthServiceImpl @Inject()(jwtConfig: JwtConfig,
                                   algorithm: JwtHmacAlgorithm) extends JwtAuthService {

  override def createAccessToken(content: JwtContent): String =
    Jwt.encode(JwtClaim(Json.toJson(content).toString()).issuedNow.expiresIn(jwtConfig.accessTokenExpiration), jwtConfig.secret, algorithm)

  override def createRefreshToken(content: JwtContent, secret: String): String =
    Jwt.encode(JwtClaim(Json.toJson(content).toString()).issuedNow.expiresIn(jwtConfig.refreshTokenExpiration), secret, algorithm)

  override def createTokens(content: JwtContent, secret: String): Tokens =
    Tokens(createAccessToken(content), createRefreshToken(content, secret))

  override def decodeContent(token: String): Try[JwtContent] = withExceptionTransforming {
    Jwt.decodeRaw(token, JwtOptions(signature = false)).map(Json.parse(_).as[JwtContent])
  }

  override def decodeAccessToken(token: String): Try[JwtContent] = withExceptionTransforming {
    Jwt.decodeRaw(token, jwtConfig.secret, Seq(algorithm)).map(Json.parse(_).as[JwtContent])
  }

  override def decodeRefreshToken(token: String, secret: String): Try[JwtContent] = withExceptionTransforming {
    Jwt.decodeRaw(token, secret, Seq(algorithm)).map(Json.parse(_).as[JwtContent])
  }

  private def withExceptionTransforming[T](maybeResult: Try[T]): Try[T] = maybeResult.recover {
    case _: JwtExpirationException => throw InvalidToken("Token is expired")
    case error@_ => throw InvalidToken(error.getMessage)
  }
}