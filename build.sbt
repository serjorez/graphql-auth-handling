
name := "graphqlHttpContextHandling"

version := "0.1"

scalaVersion := "2.12.8"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += "Atlassian Maven Repository" at "https://maven.atlassian.com/content/repositories/atlassian-public/"

libraryDependencies ++= Seq(
  guice,
  evolutions,
  "org.sangria-graphql" %% "sangria-play-json" % "1.0.5",
  "org.sangria-graphql" %% "sangria" % "1.4.2",

  "com.h2database" % "h2" % "1.4.197",

  "com.typesafe.play" %% "play-slick" % "4.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "4.0.0",
  "com.typesafe.slick" %% "slick" % "3.3.0",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.0"
)