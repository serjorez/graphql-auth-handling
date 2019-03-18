package graphql.resolvers

import com.google.inject.Inject
import models.errors.{NotFound, Unauthenticated}
import models.jwt.{JwtContent, Tokens}
import models.User
import org.mindrot.jbcrypt.BCrypt
import repositories.UserRepository
import services.JwtService

import scala.concurrent.{ExecutionContext, Future}

/**
  * A resolver that does actions on the Post entity.
  *
  * @param userRepository   a repository that provides basic operations for the User entity
  * @param jwtService       a service that provides operations with jwt tokens
  * @param executionContext a thread pool to asynchronously execute operations
  */
class UserResolver @Inject()(userRepository: UserRepository,
                             jwtService: JwtService)
                            (implicit executionContext: ExecutionContext) {

  /**
    * Registers a new user.
    *
    * @param username a username of the user
    * @param password a password of the user
    * @return tokens entity
    */
  def register(username: String, password: String): Future[Tokens] =
    userRepository.create(
      User(
        role = User.role.USER,
        password = BCrypt.hashpw(password, BCrypt.gensalt),
        username = username)
    ).map(user => jwtService.createTokens(JwtContent(user.id.get), user.password))

  /**
    * Login a new user.
    *
    * @param username a username of the user
    * @param password a password of the user
    * @return tokens entity
    */
  def login(username: String, password: String): Future[Tokens] =
    userRepository.findByUsername(username).flatMap {
      case Some(user) =>
        if (BCrypt.checkpw(password, user.password)) {
          Future.successful(jwtService.createTokens(JwtContent(user.id.get), user.password))
        } else Future.failed(Unauthenticated("Wrong password."))
      case None => Future.failed(NotFound(s"User with username: [$username] not found."))
    }

  /**
    * Finds all users.
    *
    * @return a list of users
    */
  def users(): Future[List[User]] = userRepository.findAll()

  /**
    * Finds a user by username.
    *
    * @param username an username of the user
    * @return found user
    */
  def findUser(username: String): Future[Option[User]] = userRepository.findByUsername(username)

  /**
    * Returns current user by id.
    *
    * @param userId an id of the user
    * @return found user
    */
  def currentUser(userId: Long): Future[User] =
    userRepository.find(userId).flatMap {
      case Some(user) => Future.successful(user)
      case None => Future.failed(NotFound(s"Cannot found current user, id: [$userId] is invalid."))
    }
}