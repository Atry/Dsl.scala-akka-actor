import Ordering.Implicits._

libraryDependencies += {
  if (scalaBinaryVersion.value == "2.11") {
    "org.akka-js" %%% "akkajsactor" % "1.2.5.26"
  } else if (VersionNumber(scalaJSVersion).numbers < Seq(1)) {
    "org.akka-js" %%% "akkajsactor" % "2.2.6.3"
  } else {
    "org.akka-js" %%% "akkajsactor" % "2.2.6.5"
  }
}

libraryDependencies += {
  if (scalaBinaryVersion.value == "2.11") {
    "org.akka-js" %%% "akkajstestkit" % "1.2.5.26" % Test
  } else if (VersionNumber(scalaJSVersion).numbers < Seq(1)) {
    "org.akka-js" %%% "akkajstestkit" % "2.2.6.3" % Test
  } else {
    "org.akka-js" %%% "akkajstestkit" % "2.2.6.5" % Test
  }
}
