import sbtcrossproject.CrossPlugin.autoImport.crossProject

ThisBuild / organization := "com.yang-bo"

lazy val `actor` = crossProject(JSPlatform, JVMPlatform).in(file("dsl/keywords/akka/actor"))

lazy val `actorJVM` = `actor`.jvm

lazy val `actorJS` = `actor`.js

lazy val `typed` =
  crossProject(JSPlatform, JVMPlatform).in(file("dsl/domains/akka/actor/typed")).dependsOn(`actor`)

lazy val `typedJVM` = `typed`.jvm

lazy val `typedJS` = `typed`.js

enablePlugins(ScalaUnidocPlugin)

unidocProjectFilter in ScalaUnidoc in unidoc := {
  val jvmProjects = for {
    (projectRef, definition) <- loadedBuild.value.allProjectRefs
    if !definition.autoPlugins.contains(ScalaJSPlugin)
  } yield projectRef
  inProjects(jvmProjects: _*)
}

scalacOptions ++= {
  scalaBinaryVersion.value match {
    case "2.11" =>
      Some("-Xexperimental")
    case _ =>
      None
  }
}

publish / skip := true
