package services

import com.google.inject.Inject
import graphql.GraphQLContext
import models.errors.Unauthorized

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Provides authorizing functions.
  */
class AuthorizeServiceImpl @Inject()(jwtAuthService: JwtAuthService) extends AuthorizeService {

  /** @inheritdoc*/
  override def withAuthorization[T](context: GraphQLContext)(callback: Long => Future[T]): Future[T] =
    context.requestCookies.get("accessToken") match {
      case Some(token) =>
        jwtAuthService.decodeAccessToken(token.value) match {
          case Success(content) => callback(content.id)
          case Failure(exception) => Future.failed(exception)
        }
      case None => Future.failed(Unauthorized("Cannot find access token."))
    }
}