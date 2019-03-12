package config

import java.util.concurrent.TimeUnit

import com.google.inject.{Inject, Singleton}
import com.typesafe.config.Config

/**
  * Defines a set of jwt configurations.
  */
@Singleton
class JwtConfig @Inject()(config: Config) {


  /**
    * The secret key that is used to generate the access key.
    * Must be stored on the server and never pass to client.
    */
  val secret: String = config.getString("jwt.secretKey")

  /**
    * Defines the number of days how long the access token lives.
    */
  val accessTokenExpiration: Long = config.getDuration("jwt.accessTokenExpiration", TimeUnit.DAYS)

  /**
    * Defines the number of days how long the refresh token lives.
    */
  val refreshTokenExpiration: Long = config.getDuration("jwt.refreshTokenExpiration", TimeUnit.DAYS)
}