package graphql.resolvers

import com.google.inject.Inject
import graphql.GraphQLContext
import models.errors.{NotFound, Unauthenticated}
import models.jwt.{JwtContent, Tokens}
import models.User
import org.mindrot.jbcrypt.BCrypt
import repositories.UserRepository
import services.{AuthorizationService, JwtAuthService}

import scala.concurrent.{ExecutionContext, Future}

class UserResolver @Inject()(userRepository: UserRepository,
                             jwtAuthService: JwtAuthService,
                             authorizeService: AuthorizationService)
                            (implicit executionContext: ExecutionContext) {

  import authorizeService._

  def register(login: String, password: String)
              (context: GraphQLContext): Future[Tokens] = {
    userRepository.create(
      User(
        role = User.role.USER,
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

  def users(): Future[List[User]] = userRepository.findAll()

  def findUser(login: String): Future[Option[User]] = userRepository.findByLogin(login)

  def currentUser(context: GraphQLContext): Future[User] = withAuthorization(context) {
    userId =>
      userRepository.find(userId).flatMap {
        case Some(user) => Future.successful(user)
        case None => Future.failed(NotFound(s"Cannot found current user, id: [$userId] is invalid"))
      }
  }
}