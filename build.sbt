name := "alohura"

organization := "fr.applicius"

version := "1.0.3-SNAPSHOT"

scalaVersion := "2.10.0"

resolvers += "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"

libraryDependencies += "org.specs2" %% "specs2" % "1.14"

libraryDependencies += "com.jsuereth" %% "scala-arm" % "1.3"

publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))
