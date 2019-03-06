package modules

import com.google.inject.{AbstractModule, Provides}
import models.User
import repositories.{Repository, UserRepository}

class UserModule extends AbstractModule {

  /**
    * Provides an implementation of the Repository[User] trait.
    *
    * @param userRepository an instance of the UserRepository class
    * @return an instance of the UserRepository class that implements Repository[User] trait
    */
  @Provides
  def provideUserRepository(userRepository: UserRepository): Repository[User] = userRepository
}