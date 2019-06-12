libraryDependencies ++= {
  if (scalaBinaryVersion.value == "2.13") {
    Nil
  } else {
    Seq(
      "org.akka-js" %%% "akkajsactortyped" % "1.2.5.21",
      "org.akka-js" %%% "akkajstypedtestkit" % "1.2.5.21" % Test,
      "org.akka-js" %%% "akkajstestkit" % "1.2.5.21" % Test,
    )
  }
}

skip := scalaBinaryVersion.value == "2.13"
