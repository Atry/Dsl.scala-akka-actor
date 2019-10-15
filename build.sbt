import sbtcrossproject.CrossPlugin.autoImport.crossProject

ThisBuild / organization := "com.yang-bo"

lazy val ReceiveMessage = crossProject(JSPlatform, JVMPlatform).in(file("dsl/keywords/akka/actor/ReceiveMessage"))

lazy val ReceiveMessageJVM = ReceiveMessage.jvm

lazy val ReceiveMessageJS = ReceiveMessage.js

lazy val typed =
  crossProject(JSPlatform, JVMPlatform).in(file("dsl/domains/akka/actor/typed")).dependsOn(ReceiveMessage)

lazy val typedJVM = typed.jvm

lazy val typedJS = typed.js

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

ThisBuild / useCoursier := false
