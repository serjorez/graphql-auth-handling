package modules

import com.google.inject.{AbstractModule, Provides, Scopes}
import pdi.jwt.JwtAlgorithm
import pdi.jwt.algorithms.JwtHmacAlgorithm
import services.{JwtService, JwtServiceImpl}

/**
  * The Guice module with bindings related to JWT tokens.
  */
class JwtModule extends AbstractModule {

  /**
    * A method where bindings should be defined.
    */
  override def configure(): Unit = {
    bind(classOf[JwtService]).to(classOf[JwtServiceImpl]).in(Scopes.SINGLETON)
  }

  /**
    * Provides an implementation of the JwtHmacAlgorithm trait.
    *
    * @return an instance of the HS512 that implements JwtHmacAlgorithm trait
    */
  @Provides
  def algorithm: JwtHmacAlgorithm = {
    JwtAlgorithm.HS512
  }
}