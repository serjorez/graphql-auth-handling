package graphql.resolvers

import com.google.inject.Inject
import models.errors.NotFound
import models.jwt.{JwtContent, Tokens}
import repositories.UserRepository
import services.JwtAuthService

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class JwtResolver @Inject()(userRepository: UserRepository, jwtAuthService: JwtAuthService)
                           (implicit executionContext: ExecutionContext) {

  def refreshTokens(refreshToken: String): Future[Tokens] =
    jwtAuthService.decodeContent(refreshToken) match {
      case Success(content) =>
        userRepository.find(content.id).flatMap {
          case Some(user) =>
            jwtAuthService.decodeRefreshToken(refreshToken, user.password) match {
              case Success(_) =>
                Future.successful(
                  Tokens(
                    accessToken = jwtAuthService.createAccessToken(JwtContent(content.id)),
                    refreshToken = jwtAuthService.createRefreshToken(JwtContent(content.id), user.password)
                  )
                )
              case Failure(exception) => Future.failed(exception)
            }
          case None => Future.failed(NotFound(s"User with id = ${content.id} not found."))
        }
      case Failure(exception) => Future.failed(exception)
    }
}