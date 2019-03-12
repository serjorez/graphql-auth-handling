package modules

import com.google.inject.{AbstractModule, Scopes}
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
}