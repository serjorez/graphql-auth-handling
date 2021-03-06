package repositories

import com.google.inject.{Inject, Singleton}
import models.errors.{AlreadyExists, NotFound}
import models.User
import modules.AppDatabase
import slick.lifted

import scala.concurrent.{ExecutionContext, Future}

/**
  * Provides basic CRUD operations on the User entity.
  *
  * @param database         a database access object
  * @param executionContext a thread pool to asynchronously execute operations
  */
@Singleton
class UserRepository @Inject()(val database: AppDatabase)
                               (implicit executionContext: ExecutionContext) extends Repository[User] {

  /**
    * Specific database
    */
  val db = database.db

  /**
    * Specific database profile
    */
  val profile = database.profile

  import profile.api._

  def userQuery: TableQuery[User.Table] = lifted.TableQuery[User.Table]

  /** @inheritdoc*/
  override def create(user: User): Future[User] = db.run {
    Actions.create(user)
  }

  /** @inheritdoc*/
  override def find(id: Long): Future[Option[User]] = db.run {
    Actions.find(id)
  }

  /** @inheritdoc*/
  override def findAll(): Future[List[User]] = db.run {
    Actions.findAll()
  }

  /** @inheritdoc*/
  override def update(user: User): Future[User] = db.run {
    Actions.update(user)
  }

  /** @inheritdoc*/
  override def delete(id: Long): Future[Boolean] = db.run {
    Actions.delete(id)
  }

  /**
    * Returns a user by username.
    *
    * @param username username of the user
    * @return found user
    */
  def findByUsername(username: String): Future[Option[User]] = db.run {
    Actions.findByUsername(username)
  }


  /**
    * Provides implementation for CRUD operations on the User entity.
    */
  object Actions {

    def create(user: User): DBIO[User] = for {
      maybeUser <- if (user.id.isEmpty) DBIO.successful(None) else find(user.id.get)
      _ <- maybeUser.fold(DBIO.successful(None)) {
        _ => DBIO.failed(AlreadyExists(s"User with id = ${user.id} already exists."))
      }
      userWithSameUsername <- userQuery.filter(_.username === user.username).result
      id <- if (userWithSameUsername.lengthCompare(1) < 0) userQuery returning userQuery.map(_.id) += user else {
        DBIO.failed(AlreadyExists(s"User with username = '${user.username}' already exists."))
      }
    } yield user.copy(id = Some(id))

    def find(id: Long): DBIO[Option[User]] = userQuery.filter(_.id === id).result.headOption

    def findAll(): DBIO[List[User]] = userQuery.result.map(_.toList)

    def update(user: User): DBIO[User] = for {
      maybeUserWithSameUsername <- userQuery.filter(_.username === user.username).result
      _ <- if (maybeUserWithSameUsername.lengthCompare(1) < 0) DBIO.successful(None) else {
        DBIO.failed(AlreadyExists(s"User with username = '${user.username}' already exists."))
      }
      count <- userQuery.filter(_.id === user.id).update(user)
      _ <- count match {
        case 0 => DBIO.failed(NotFound(s"Can't find user with id = ${user.id}."))
        case _ => DBIO.successful(())
      }
    } yield user

    def delete(id: Long): DBIO[Boolean] = userQuery.filter(_.id === id).delete.map(_ == 1)

    def findByUsername(username: String): DBIO[Option[User]] =
      userQuery.filter(user => user.username === username).result.headOption
  }

}
