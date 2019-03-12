package graphql.resolvers

import com.google.inject.Inject
import models.errors.NotFound
import models.jwt.{JwtContent, Tokens}
import repositories.UserRepository
import services.JwtService

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
  * A resolver that does actions with JWT tokens.
  *
  * @param userRepository   a repository that provides basic operations for the User entity
  * @param jwtService   a service that provides operations with jwt tokens
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
  def refreshTokens(refreshToken: String): Future[Tokens] =
    jwtService.decodeContent(refreshToken) match {
      case Success(content) =>
        userRepository.find(content.id).flatMap {
          case Some(user) =>
            jwtService.decodeRefreshToken(refreshToken, user.password) match {
              case Success(_) =>
                Future.successful(
                  Tokens(
                    accessToken = jwtService.createAccessToken(JwtContent(content.id)),
                    refreshToken = jwtService.createRefreshToken(JwtContent(content.id), user.password)
                  )
                )
              case Failure(exception) => Future.failed(exception)
            }
          case None => Future.failed(NotFound(s"User with id = ${content.id} not found."))
        }
      case Failure(exception) => Future.failed(exception)
    }
}