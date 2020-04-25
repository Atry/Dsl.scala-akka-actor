enablePlugins(SubdirectoryOrganization)

libraryDependencies += "com.thoughtworks.dsl" %%% "keywords-catch" % "1.5.2" % Optional

libraryDependencies += "com.thoughtworks.dsl" %%% "dsl" % "1.5.2"

addCompilerPlugin("com.thoughtworks.dsl" %% "compilerplugins-bangnotation" % "1.5.2")

addCompilerPlugin("com.thoughtworks.dsl" %% "compilerplugins-reseteverywhere" % "1.5.2")

libraryDependencies += "org.scalatest" %%% "scalatest" % "3.1.1" % Test

libraryDependencies ++= {
  if (scalaBinaryVersion.value == "2.13") {
    None
  } else {
    Some("org.scalamock" %%% "scalamock" % "4.4.0" % Test)
  }
}

// Disable tests in scala 2.13 due to lack of ScalaMock
sourceGenerators in Test := {
  (sourceGenerators in Test).value.filterNot { sourceGenerator =>
    scalaBinaryVersion.value == "2.13" &&
    sourceGenerator.info.get(taskDefinitionKey).exists { scopedKey: ScopedKey[_] =>
      scopedKey.key == generateExample.key
    }
  }
}

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
