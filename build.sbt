import sbtcrossproject.CrossPlugin.autoImport.crossProject

sonatypeProfileName := "com.yang-bo"

ThisBuild / organization := s"${sonatypeProfileName.value}.dsl.domains.akka.actor"

lazy val `dsl-keywords-akka-actor-NextMessage` = crossProject(JSPlatform, JVMPlatform).build()

lazy val `dsl-keywords-akka-actor-NextMessageJVM` = `dsl-keywords-akka-actor-NextMessage`.jvm

lazy val `dsl-keywords-akka-actor-NextMessageJS` = `dsl-keywords-akka-actor-NextMessage`.js

enablePlugins(ScalaUnidocPlugin)

unidocProjectFilter in ScalaUnidoc in unidoc := {
  val jvmProjects = for {
    (projectRef, definition) <- loadedBuild.value.allProjectRefs
    if !definition.autoPlugins.contains(ScalaJSPlugin)
  } yield projectRef
  inProjects(jvmProjects: _*)
}

publish / skip := false
