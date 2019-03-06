package graphql.resolvers

import com.google.inject.Inject
import graphql.GraphQLContext
import models.errors.{NotFound, Unauthenticated}
import models.jwt.{JwtContent, Tokens}
import models.User
import org.mindrot.jbcrypt.BCrypt
import repositories.UserRepository
import services.JwtAuthService

import scala.concurrent.{ExecutionContext, Future}

class UserResolver @Inject()(userRepository: UserRepository,
                             jwtAuthService: JwtAuthService,
                             implicit val executionContext: ExecutionContext) {

  def register(login: String, password: String, role: Option[String])
              (context: GraphQLContext): Future[Tokens] = {
    userRepository.create(
      User(
        role = role.getOrElse(User.role.USER),
        password = BCrypt.hashpw(password, BCrypt.gensalt),
        login = login)
    ).map(user => jwtAuthService.createTokens(JwtContent(user.id.get), user.password))
  }

  def login(login: String, password: String)
           (context: GraphQLContext): Future[Tokens] = {
    userRepository.findByLogin(login).flatMap {
      case Some(user) =>
        if (BCrypt.checkpw(password, user.password)) {
          Future.successful(jwtAuthService.createTokens(JwtContent(user.id.get), user.password))
        } else Future.failed(Unauthenticated(""))
      case None => Future.failed(NotFound(s"User with login: [$login] not found."))
    }
  }

  def findUser(login: String): Future[Option[User]] = userRepository.findByLogin(login)
}