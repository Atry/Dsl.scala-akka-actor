libraryDependencies += {
  if (scalaBinaryVersion.value == "2.11") {
    "com.typesafe.akka" %% "akka-actor-typed" % "2.5.21"
  } else {
    "com.typesafe.akka" %% "akka-actor-typed" % "2.5.23"
  }
}
libraryDependencies += {
  if (scalaBinaryVersion.value == "2.11") {
    "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.5.21" % Test
  } else {
    "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.5.23" % Test
  }
}

// Only enable tests for JVM due to https://github.com/akka-js/akka.js/issues/96
enablePlugins(Example)

import meta._
examplePackageRef := q"com.yang_bo.dsl.domains.akka.actor"
