enablePlugins(SubdirectoryOrganization)

libraryDependencies += "com.thoughtworks.dsl" %%% "dsl" % "1.4.0"

addCompilerPlugin("com.thoughtworks.dsl" %% "compilerplugins-bangnotation" % "1.4.0")

libraryDependencies += "com.thoughtworks.dsl" %% "compilerplugins-bangnotation" % "1.4.0" % Optional // For Scaladoc

addCompilerPlugin("com.thoughtworks.dsl" %% "compilerplugins-reseteverywhere" % "1.4.0")

libraryDependencies += "com.thoughtworks.dsl" %% "compilerplugins-reseteverywhere" % "1.4.0" % Optional // For Scaladoc

libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value % Optional // For Scaladoc

libraryDependencies += "org.scalatest" %%% "scalatest" % "3.0.8" % Test

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
  case ctor"_root_.org.scalatest.FreeSpec" =>
    Seq(
      ctor"_root_.akka.testkit.TestKit(_root_.akka.actor.ActorSystem())",
      ctor"_root_.org.scalatest.FreeSpecLike",
      ctor"_root_.com.yang_bo.dsl.keywords.akka.actor.ShutdownAfterAll",
      ctor"_root_.akka.testkit.ImplicitSender",
    )
  case otherTrait =>
    Seq(otherTrait)
}