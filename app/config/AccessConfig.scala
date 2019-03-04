package config

import com.google.inject.Singleton

/**
  * Defines a set of configurations for authentication.
  */
@Singleton
class AccessConfig {

  /**
    * Get an access token, that client must have to perform administrator operations.
    *
    * @return an administrator token
    */
  def getAdminAccessToken = "some_token"
}