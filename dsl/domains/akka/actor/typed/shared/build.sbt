enablePlugins(SubdirectoryOrganization)

libraryDependencies += "com.thoughtworks.dsl" %%% "keywords-catch" % "1.5.3" % Optional

libraryDependencies += "com.thoughtworks.dsl" %%% "dsl" % "1.5.3"

addCompilerPlugin("com.thoughtworks.dsl" %% "compilerplugins-bangnotation" % "1.5.3")

addCompilerPlugin("com.thoughtworks.dsl" %% "compilerplugins-reseteverywhere" % "1.5.3")

libraryDependencies += "org.scalatest" %%% "scalatest" % "3.1.3" % Test

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

examplePackageRef := q"com.yang_bo.dsl.domains.akka.actor"

exampleSuperTypes += ctor"org.scalamock.scalatest.MockFactory"
