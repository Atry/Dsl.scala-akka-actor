import Ordering.Implicits._

libraryDependencies += {
  if (scalaBinaryVersion.value == "2.11") {
    "org.akka-js" %%% "akkajsactortyped" % "1.2.5.26"
  } else if (VersionNumber(scalaJSVersion).numbers < Seq(1)) {
    "org.akka-js" %%% "akkajsactortyped" % "2.2.6.3"
  } else {
    "org.akka-js" %%% "akkajsactortyped" % "2.2.6.4"
  }
}

libraryDependencies += {
  if (scalaBinaryVersion.value == "2.11") {
    "org.akka-js" %%% "akkajstypedtestkit" % "1.2.5.26" % Test
  } else if (VersionNumber(scalaJSVersion).numbers < Seq(1)) {
    "org.akka-js" %%% "akkajstypedtestkit" % "2.2.6.3" % Test
  } else {
    "org.akka-js" %%% "akkajstypedtestkit" % "2.2.6.4" % Test
  }
}

libraryDependencies += {
  if (scalaBinaryVersion.value == "2.11") {
    "org.akka-js" %%% "akkajstestkit" % "1.2.5.26" % Test
  } else if (VersionNumber(scalaJSVersion).numbers < Seq(1)) {
    "org.akka-js" %%% "akkajstestkit" % "2.2.6.3" % Test
  } else {
    "org.akka-js" %%% "akkajstestkit" % "2.2.6.4" % Test
  }
}

libraryDependencies ++= {
  if (VersionNumber(scalaJSVersion).numbers >= Seq(1)) {
    None
  } else {
    Some("org.scalamock" %%% "scalamock" % "5.0.0" % Test)
  }
}

// Disable tests in Scala.js 1.x due to lack of ScalaMock
sourceGenerators in Test := {
  (sourceGenerators in Test).value.filterNot { sourceGenerator =>
    VersionNumber(scalaJSVersion).numbers >= Seq(1) &&
    sourceGenerator.info.get(taskDefinitionKey).exists { scopedKey: ScopedKey[_] =>
      scopedKey.key == generateExample.key
    }
  }
}
