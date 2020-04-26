import Ordering.Implicits._

libraryDependencies += {
  if (scalaBinaryVersion.value == "2.11") {
    "org.akka-js" %%% "akkajsactortyped" % "1.2.5.26"
  } else {
    "org.akka-js" %%% "akkajsactortyped" % "2.2.6.4"
  }
}

libraryDependencies += {
  if (scalaBinaryVersion.value == "2.11") {
    "org.akka-js" %%% "akkajstypedtestkit" % "1.2.5.26" % Test
  } else {
    "org.akka-js" %%% "akkajstypedtestkit" % "2.2.6.4" % Test
  }
}

libraryDependencies += {
  if (scalaBinaryVersion.value == "2.11") {
    "org.akka-js" %%% "akkajstestkit" % "1.2.5.26" % Test
  } else {
    "org.akka-js" %%% "akkajstestkit" % "2.2.6.4" % Test
  }
}