libraryDependencies += "com.thoughtworks.dsl" %%% "dsl" % "1.3.2"

addCompilerPlugin("com.thoughtworks.dsl" %% "compilerplugins-bangnotation" % "1.3.2")

addCompilerPlugin("com.thoughtworks.dsl" %% "compilerplugins-reseteverywhere" % "1.3.2")

libraryDependencies += "org.scalatest" %%% "scalatest" % "3.0.8"

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