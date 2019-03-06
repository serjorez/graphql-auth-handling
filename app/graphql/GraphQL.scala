package graphql

import com.google.inject.{Inject, Singleton}
import graphql.schema.{JwtSchema, PostSchema, UserSchema}
import sangria.schema.{ObjectType, fields}

/**
  * Defines the global GraphQL-related objects of the application.
  */
@Singleton
class GraphQL @Inject()(postSchema: PostSchema,
                        userSchema: UserSchema,
                        jwtSchema: JwtSchema) {

  /**
    * The constant that signifies the maximum allowed depth of query.
    */
  val maxQueryDepth = 15

  /**
    * The constant that signifies the maximum allowed complexity of query.
    */
  val maxQueryComplexity = 1000

  /**
    * The GraphQL schema of the application.
    */
  val Schema = sangria.schema.Schema(
    query = ObjectType("Query",
      fields(
        postSchema.Queries ++
          userSchema.Queries: _*
      )
    ),
    mutation = Some(
      ObjectType("Mutation",
        fields(
          postSchema.Mutations ++
            userSchema.Mutations ++
            jwtSchema.Mutations: _*
        )
      )
    )
  )
}