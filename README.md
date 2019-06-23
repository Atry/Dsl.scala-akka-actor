# Akka Actor support for Dsl.scala

[![Build Status](https://travis-ci.org/Atry/Dsl.scala-akka-actor.svg?branch=master)](https://travis-ci.org/Atry/Dsl.scala-akka-actor)
[![Scaladoc](https://javadoc.io/badge/com.yang-bo.dsl.domains.akka.actor/typed_2.11.svg?label=scaladoc)](https://javadoc.io/page/com.yang-bo.dsl.domains.akka.actor/typed_2.11/latest/com/yang_bo/dsl/package.html)

<a href="https://search.maven.org/search?q=g:com.yang-bo.dsl.keywords.akka.actor%20a:receivemessage_*"><img src="https://img.shields.io/maven-central/v/com.yang-bo.dsl.keywords.akka.actor/receivemessage_2.13.svg?label=libraryDependencies+%2B=+%22com.yang-bo.dsl.keywords.akka.actor%22+%25%25+%22receivemessage%22+%25"/></a>
<a href="https://search.maven.org/search?q=g:com.yang-bo.dsl.domains.akka.actor%20a:typed_*"><img src="https://img.shields.io/maven-central/v/com.yang-bo.dsl.domains.akka.actor/typed_2.13.svg?label=libraryDependencies+%2B=+%22com.yang-bo.dsl.domains.akka.actor%22+%25%25+%22typed%22+%25"/></a>


**Dsl.scala-akka-actor** contains utilities to integrate Akka Actor with [Dsl.scala](https://github.com/ThoughtWorksInc/Dsl.scala).

There are two libraries in this project:

* [com.yang-bo.dsl.keywords.akka.actor::receivemessage](https://javadoc.io/page/com.yang-bo.dsl.keywords.akka.actor/receivemessage_2.12/latest/com/yang_bo/dsl/keywords/akka/actor/ReceiveMessage.html) provides the direct style DSL to receive messages in Akka Actors.
* [com.yang-bo.dsl.domains.akka.actor::typed](https://javadoc.io/page/com.yang-bo.dsl.domains.akka.actor/typed_2.12/latest/com/yang_bo/dsl/domains/akka/actor/typed%24.html) enables the above direct style DSL to receive messages in typed Akka Actors.
