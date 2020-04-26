enablePlugins(SubdirectoryOrganization)

libraryDependencies += "com.thoughtworks.dsl" %%% "dsl" % "1.5.3"

addCompilerPlugin("com.thoughtworks.dsl" %% "compilerplugins-bangnotation" % "1.5.3")

libraryDependencies += "com.thoughtworks.dsl" %% "compilerplugins-bangnotation" % "1.5.3" % Optional // For Scaladoc

addCompilerPlugin("com.thoughtworks.dsl" %% "compilerplugins-reseteverywhere" % "1.5.3")

libraryDependencies += "com.thoughtworks.dsl" %% "compilerplugins-reseteverywhere" % "1.5.3" % Optional // For Scaladoc

libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value % Optional // For Scaladoc

libraryDependencies += "org.scalatest" %%% "scalatest" % "3.1.1" % Test

scalacOptions ++= {
  scalaBinaryVersion.value match {
    case "2.11" =>
      Some("-Xexperimental")
    case _ =>
      None
  }
}

enablePlugins(Example)

import meta._
examplePackageRef := q"com.yang_bo.dsl.keywords.akka.actor"

import meta._
exampleSuperTypes := exampleSuperTypes.value.flatMap {
  case ctor"_root_.org.scalatest.freespec.AnyFreeSpec" =>
    Seq(
      ctor"_root_.akka.testkit.TestKit(_root_.akka.actor.ActorSystem())",
      ctor"_root_.org.scalatest.freespec.AnyFreeSpecLike",
      ctor"_root_.com.yang_bo.dExtractor.scalal.keywords.akka.actor.ShutdownAfterAll",
      ctor"_root_.akka.testkit.ImplicitSender",
    )
  case otherTrait =>
    Seq(otherTrait)
}