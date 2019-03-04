package modules

import com.google.inject.{AbstractModule, Provides, Scopes}
import models.Post
import repositories.{PostRepository, Repository}
import services.{PostsAuthorizeService, PostsAuthorizeServiceImpl}
import validators.{AdminAccessValidator, AdminAccessValidatorImpl, PostValidator, PostValidatorImpl}

/**
  * The Guice module with bindings related to the Post entity.
  */
class PostModule extends AbstractModule {

  /**
    * A method where bindings should be defined.
    */
  override def configure(): Unit = {
    bind(classOf[PostValidator]).to(classOf[PostValidatorImpl]).in(Scopes.SINGLETON)
    bind(classOf[PostsAuthorizeService]).to(classOf[PostsAuthorizeServiceImpl]).in(Scopes.SINGLETON)
    bind(classOf[AdminAccessValidator]).to(classOf[AdminAccessValidatorImpl]).in(Scopes.SINGLETON)
  }

  /**
    * Provides an implementation of the Repository[Post] trait.
    *
    * @param postRepository an instance of the PostRepository class
    * @return an instance of the PostRepository class that implements Repository[Post] trait
    */
  @Provides
  def providePostRepository(postRepository: PostRepository): Repository[Post] = postRepository
}