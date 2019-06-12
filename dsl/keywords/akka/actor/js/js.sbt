libraryDependencies ++= {
  if (scalaBinaryVersion.value == "2.13") {
    Nil
  } else {
    Seq(
      "org.akka-js" %%% "akkajsactor" % "1.2.5.21",
      "org.akka-js" %%% "akkajstestkit" % "1.2.5.21" % Test,
    )
  }
}

skip := scalaBinaryVersion.value == "2.13"
