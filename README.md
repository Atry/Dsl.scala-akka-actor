# Akka Actor support for Dsl.scala

[![Build Status](https://travis-ci.org/Atry/Dsl.scala-akka-actor.svg?branch=master)](https://travis-ci.org/Atry/Dsl.scala-akka-actor)



**Dsl.scala-akka-actor** contains utilities to integrate Akka Actor with [Dsl.scala](https://github.com/ThoughtWorksInc/Dsl.scala).

## Modules

There are two modules in this project.

### `ReceiveMessage`
<a href="https://search.maven.org/search?q=g:com.yang-bo.dsl.keywords.akka.actor%20a:receivemessage_*"><img src="https://img.shields.io/maven-central/v/com.yang-bo.dsl.keywords.akka.actor/receivemessage_2.13.svg?label=libraryDependencies+%2B=+%22com.yang-bo.dsl.keywords.akka.actor%22+%25%25+%22receivemessage%22+%25"/></a> [![Scaladoc](https://javadoc.io/badge/com.yang-bo.dsl.keywords.akka.actor/receivemessage_2.13.svg?label=Documentation)](https://javadoc.io/page/com.yang-bo.dsl.keywords.akka.actor/receivemessage_2.13/latest/com/yang_bo/dsl/keywords/akka/actor/ReceiveMessage.html)

`ReceiveMessage` provides the direct style DSL to receive messages in Akka Actors.


### `typed`
<a href="https://search.maven.org/search?q=g:com.yang-bo.dsl.domains.akka.actor%20a:typed_*"><img src="https://img.shields.io/maven-central/v/com.yang-bo.dsl.domains.akka.actor/typed_2.13.svg?label=libraryDependencies+%2B=+%22com.yang-bo.dsl.domains.akka.actor%22+%25%25+%22typed%22+%25"/></a> [![Scaladoc](https://javadoc.io/badge/com.yang-bo.dsl.domains.akka.actor/typed_2.13.svg?label=Documentation)](https://javadoc.io/page/com.yang-bo.dsl.domains.akka.actor/typed_2.13/latest/com/yang_bo/dsl/domains/akka/actor/typed$.html)

`typed` enables the above direct style DSL in typed Akka Actors.
