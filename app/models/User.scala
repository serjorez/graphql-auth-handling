package models

import slick.jdbc.H2Profile.api.{Table => SlickTable, _}
import slick.lifted.{Tag => SlickTag}

case class User(id: Option[Long] = None,
                role: String = User.role.USER,
                login: String,
                password: String)

object User extends ((Option[Long], String, String, String) => User) {

  class Table(slickTag: SlickTag) extends SlickTable[User](slickTag, "USERS") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

    def role = column[String]("ROLE")

    def login = column[String]("LOGIN")

    def password = column[String]("PASSWORD")

    def * = (id.?, role, login, password).mapTo[User]
  }

  object role {
    val USER: String = "user"
    val ADMIN: String = "admin"
  }

}