package graphql.resolvers

import com.google.inject.Inject
import models.errors.NotFound
import models.jwt.{JwtContent, Tokens}
import repositories.UserRepository
import services.JwtService

import scala.concurrent.{ExecutionContext, Future}

/**
  * A resolver that does actions with JWT tokens.
  *
  * @param userRepository   a repository that provides basic operations for the User entity
  * @param jwtService       a service that provides operations with jwt tokens
  * @param executionContext a thread pool to asynchronously execute operations
  */
class JwtResolver @Inject()(userRepository: UserRepository, jwtService: JwtService)
                           (implicit executionContext: ExecutionContext) {

  /**
    * Refreshes a jwt tokens.
    *
    * @param refreshToken a refresh token
    * @return tokens entity
    */
  def refreshTokens(refreshToken: String): Future[Tokens] = for {
    content <- Future(jwtService.decodeContent(refreshToken).get)
    mayBeUser <- userRepository.find(content.id)
    user <- mayBeUser.map(Future.successful).getOrElse(Future.failed(NotFound(s"User with id = ${content.id} not found.")))
    _ <- Future(jwtService.decodeRefreshToken(refreshToken, user.password).get)
  } yield Tokens(
    accessToken = jwtService.createAccessToken(JwtContent(content.id)),
    refreshToken = jwtService.createRefreshToken(JwtContent(content.id), user.password)
  )
}