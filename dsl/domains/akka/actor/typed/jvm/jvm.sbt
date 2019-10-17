libraryDependencies += {
  if (scalaBinaryVersion.value == "2.11") {
    "com.typesafe.akka" %% "akka-actor-typed" % "2.5.21"
  } else {
    "com.typesafe.akka" %% "akka-actor-typed" % "2.5.26"
  }
}
libraryDependencies += {
  if (scalaBinaryVersion.value == "2.11") {
    "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.5.21" % Test
  } else {
    "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.5.26" % Test
  }
}
