package models

import slick.jdbc.H2Profile.api.{Table => SlickTable, _}
import slick.lifted.{Tag => SlickTag}

/**
  * The Post entity.
  *
  * @param id       an id of the post
  * @param authorId an id of the post's author
  * @param title    a title of the post
  * @param content  a content of the post
  */
case class Post(id: Option[Long] = None,
                authorId: String,
                title: String,
                content: String)

/**
  * Defined slick table for entity 'Post'
  */
object Post extends ((Option[Long], String, String, String) => Post) {

  class Table(slickTag: SlickTag) extends SlickTable[Post](slickTag, "POSTS") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

    def authorId = column[String]("AUTHOR_ID")

    def title = column[String]("TITLE")

    def content = column[String]("CONTENT")

    def * = (id.?, authorId, title, content).mapTo[Post]
  }

}