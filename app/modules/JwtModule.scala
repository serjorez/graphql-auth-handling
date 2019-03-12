package modules

import com.google.inject.{AbstractModule, Scopes}
import services.{JwtAuthService, JwtAuthServiceImpl}

/**
  * The Guice module with bindings related to JWT tokens.
  */
class JwtModule extends AbstractModule {

  /**
    * A method where bindings should be defined.
    */
  override def configure(): Unit = {
    bind(classOf[JwtAuthService]).to(classOf[JwtAuthServiceImpl]).in(Scopes.SINGLETON)
  }
}