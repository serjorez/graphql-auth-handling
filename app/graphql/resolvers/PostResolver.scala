package graphql.resolvers

import com.google.inject.Inject
import graphql.GraphQLContext
import models.{Post, User}
import models.errors.{Forbidden, NotFound}
import repositories.{Repository, UserRepository}
import services.AuthorizeService
import validators.PostValidator

import scala.concurrent.{ExecutionContext, Future}

/**
  * A resolver that does actions on the Post entity.
  *
  * @param postRepository   a repository that provides basic operations for the Post entity
  * @param postValidator    a validator that contains functions that validates the Post's fields
  * @param executionContext a thread pool to asynchronously execute operations
  */
class PostResolver @Inject()(postRepository: Repository[Post],
                             userRepository: UserRepository,
                             postValidator: PostValidator,
                             authorizeService: AuthorizeService,
                             implicit val executionContext: ExecutionContext) {

  import postValidator._
  import authorizeService._

  /**
    * Finds all posts.
    *
    * @return a list of a posts
    */
  def posts: Future[List[Post]] = postRepository.findAll()

  /**
    * Adds a post.
    *
    * @param title   a title of the post
    * @param content a content of the post
    * @param context a context of the GraphQL operation
    * @return added post
    */
  def addPost(title: String, content: String)
             (context: GraphQLContext): Future[Post] = withAuthorization(context) {
    userId =>
      withTitleValidation(title) {
        postRepository.create(Post(authorId = userId, title = title, content = content))
      }
  }

  /**
    * Finds a post by id.
    *
    * @param id an id of the post
    * @return found post
    */
  def findPost(id: Long): Future[Option[Post]] = postRepository.find(id)

  /**
    * Updates a post.
    *
    * @param id      an id of the post
    * @param title   a title of the post
    * @param content a content of the post
    * @param context a context of the GraphQL operation
    * @return updated post
    */
  def updatePost(id: Long, title: String, content: String)
                (context: GraphQLContext): Future[Post] = withAuthorization(context) {
    userId =>
      postRepository.find(id).flatMap {
        case Some(post) =>
          if (post.authorId == userId) {
            postRepository.update(Post(post.id, post.authorId, title, content))
          } else {
            userRepository.find(userId).flatMap {
              case Some(user) if user.role == User.role.ADMIN =>
                postRepository.update(Post(post.id, post.authorId, title, content))
              case _ => Future.failed(Forbidden(s"Only author of the post with id = $id can update it."))
            }
          }
        case None => Future.failed(NotFound(s"Post with id: id = $id not found."))
      }
  }

  /**
    * Deletes a post by id.
    *
    * @param id      an id of the post
    * @param context a context of the GraphQL operation
    * @return true if the post was deleted, else otherwise
    */
  def deletePost(id: Long)
                (context: GraphQLContext): Future[Boolean] = withAuthorization(context) {
    userId =>
      postRepository.find(id).flatMap {
        case Some(post) =>
          if (post.authorId == userId) {
            postRepository.delete(id)
          } else {
            userRepository.find(userId).flatMap {
              case Some(user) if user.role == User.role.ADMIN =>
                postRepository.delete(id)
              case _ => Future.failed(Forbidden(s"Only author of the post with id = $id can delete it."))
            }
          }
        case None => Future.failed(NotFound(s"Post with id: id = $id not found."))
      }
  }
}