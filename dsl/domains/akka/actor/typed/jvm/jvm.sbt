libraryDependencies += {
  if (scalaBinaryVersion.value == "2.11") {
    "com.typesafe.akka" %% "akka-actor-typed" % "2.5.21"
  } else {
    "com.typesafe.akka" %% "akka-actor-typed" % "2.6.13"
  }
}

libraryDependencies += {
  if (scalaBinaryVersion.value == "2.11") {
    "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.5.21" % Test
  } else {
    "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.6.13" % Test
  }
}

import Ordering.Implicits._

// To prevent duplicate JVM releases, only publish JVM artifacts when Scala.js version is 1.x
publish / skip := VersionNumber(scalaJSVersion).numbers < Seq(1) && VersionNumber(scalaBinaryVersion.value).numbers >= Seq(2, 12)

libraryDependencies += "org.scalamock" %%% "scalamock" % "4.4.0" % Test