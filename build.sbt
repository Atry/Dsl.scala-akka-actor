import sbtcrossproject.CrossPlugin.autoImport.crossProject

sonatypeProfileName := "com.yang-bo"

ThisBuild / organization := s"${sonatypeProfileName.value}.dsl.domains.akka.actor"

lazy val `dsl-keywords-akka-actor` = crossProject(JSPlatform, JVMPlatform).build()

lazy val `dsl-keywords-akka-actorJVM` = `dsl-keywords-akka-actor`.jvm

lazy val `dsl-keywords-akka-actorJS` = `dsl-keywords-akka-actor`.js

lazy val `dsl-domains-akka-actor-typed` =
  crossProject(JSPlatform, JVMPlatform).dependsOn(`dsl-keywords-akka-actor`)

lazy val `dsl-domains-akka-actor-typedJVM` = `dsl-domains-akka-actor-typed`.jvm

lazy val `dsl-domains-akka-actor-typedJS` = `dsl-domains-akka-actor-typed`.js

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

publish / skip := false
