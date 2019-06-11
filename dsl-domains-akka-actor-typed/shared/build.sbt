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
