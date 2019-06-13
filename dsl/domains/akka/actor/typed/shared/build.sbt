enablePlugins(SubdirectoryOrganization)

libraryDependencies += "com.thoughtworks.dsl" %%% "keywords-catch" % "1.3.2" % Optional

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
examplePackageRef := q"com.yang_bo.dsl.domains.akka.actor"
