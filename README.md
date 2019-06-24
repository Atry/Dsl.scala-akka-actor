# Akka Actor support for Dsl.scala

[![Build Status](https://travis-ci.org/Atry/Dsl.scala-akka-actor.svg?branch=master)](https://travis-ci.org/Atry/Dsl.scala-akka-actor)

**Dsl.scala-akka-actor** provides the [Akka](https://akka.io/) Actor support for [Dsl.scala](https://github.com/ThoughtWorksInc/Dsl.scala/). It is an alternative to [Akka FSM](https://doc.akka.io/docs/akka/current/fsm.html), for building actors with complex states from simple native Scala control flows.

## Getting started

This project allows !-notation to receive messages in the Akka actors, which requires `BangNotation` and `ResetEverywhere` compiler plugins, along with libraries of this project. For sbt, add the following settings to your build.sbt:

``` sbt
addCompilerPlugin("com.thoughtworks.dsl" %% "compilerplugins-bangnotation" % "latest.release")
addCompilerPlugin("com.thoughtworks.dsl" %% "compilerplugins-reseteverywhere" % "latest.release")

// The library for actors
libraryDependencies += "com.yang-bo.dsl.keywords.akka.actor" %% "receivemessage" % "latest.release"

// The library for typed actors
libraryDependencies += "com.yang-bo.dsl.domains.akka.actor" %% "typed" % "latest.release"
```

By combining !-notation and native Scala control flows, a complex state machine can be created from simple Scala code. For example, the following state machine contains two states and two transitions between them.

![Finite state machine example with comments, 1st Macguy314, reworked by Perhelion
German translation by Babakus [Public domain], via Wikipedia Commons](https://upload.wikimedia.org/wikipedia/commons/thumb/c/cf/Finite_state_machine_example_with_comments.svg/420px-Finite_state_machine_example_with_comments.svg.png)

It can be created as a simple while loop with the help of `ReceiveMessage.Partial`:

``` scala
import akka.actor.typed._

sealed trait State
case object Opened extends State
case object Closed extends State

sealed trait Transition
case class Open(response: ActorRef[State]) extends Transition
case class Close(response: ActorRef[State]) extends Transition

def doorActor: Behavior[Transition] = {
  while (true) {
    val open = !ReceiveMessage.Partial[Open]
    open.response ! Opened
    val close = !ReceiveMessage.Partial[Close]
    close.response ! Closed
  }
  throw new Exception("Unreachable code!")
}
```

The `doorActor` should reply the current state after performing an transition, which is built from some !-notation on `ReceiveMessage.Partial` keywords, which are in available in functions that return either `akka.actor.typed.Behavior` or `akka.actor.Actor.Receive`.

``` scala
import akka.actor.testkit.typed.scaladsl._
val door = BehaviorTestKit(doorActor)
val state = TestInbox[State]()
door.run(Open(state.ref))
state.expectMessage(Opened)
door.run(Close(state.ref))
state.expectMessage(Closed)
door.run(Open(state.ref))
state.expectMessage(Opened)
door.run(Close(state.ref))
state.expectMessage(Closed)
```

Previously, state machines in Akka can be created from [Akka FSM API](https://doc.akka.io/docs/akka/current/fsm.html), which consists of some domain-specific keywords like `when`, `goto` and `stay`. Unfortunately, you cannot embedded those keywords into your ordinary `if` / `while` / `match` control flows, because Akka FSM DSL is required to be split into small closures, preventing ordinary control flows from crossing the boundary of those closures.

With the help this `Dsl.scala-akka-actor` project, you can receive messages in a blocking flavor, without explicitly creating those closures. Therefore, a state machine can be described in ordinary `if` / `while` / `match` control flows, just like pseudo-code but it is runnable.

## Exception handling

To use `try` / `catch` / `finally` expressions with !-notation, the return type of enclosing function should be `Behavior !! Throwable`, as shown in the following `createDecoderActor` method. It will open an `InputStream`, read `String` from the stream, and `close` the stream in a `finally` block.

``` scala
import akka.actor.typed._
import akka.actor.typed.scaladsl._
import com.thoughtworks.dsl.Dsl.!!
import java.io._
import java.net._

sealed trait Command
case class Open(open: () => InputStream) extends Command
case class ReadObject(response: ActorRef[String]) extends Command
case object Close extends Command

class DecoderException(cause: Throwable) extends Exception(cause)

def createDecoderActor: Behavior[Command] !! Throwable = {
  while (true) {
    val inputStream = (!ReceiveMessage.Partial[Open]).open()
    try {
      val ReadObject(replyTo) = !ReceiveMessage.Partial[ReadObject]
      replyTo ! new java.io.DataInputStream(inputStream).readUTF()
      !ReceiveMessage.Partial[Close.type]
    } catch {
      case e: IOException =>
        throw new DecoderException(e)
    } finally {
      inputStream.close()
    }
  }
  throw new AssertionError("Unreachable code!")
}
```

The return type `Behavior[Command] !! Throwable` is a type alias of `(Throwable => Behavior[Command]) => Behavior[Command]`, which receives message of the type `Command`, and accepts an additional callback function to handle exceptions that are not handled in `createDecoderActor`.

``` scala
import akka.actor.testkit.typed.scaladsl._
val errorHandler = mockFunction[Throwable, Behavior[Command]]
val decoderActor = BehaviorTestKit(createDecoderActor(errorHandler))
```

Given an `InputStream` that throws an `IOException` when read from it,

``` scala
val inputStream: InputStream = mock[InputStream]
toMockFunction0(inputStream.read _).expects().throws(new IOException())
decoderActor.run(Open(() => inputStream))
```

when the `decoderActor` read a `String` from the stream, it should close the stream due to `finally` block triggered by the exception.

``` scala
val inbox = TestInbox[String]()
errorHandler.expects(where[Throwable](_.isInstanceOf[DecoderException])).returns(Behaviors.stopped)
toMockFunction0(inputStream.close _).expects().returns(()).once()
decoderActor.run(ReadObject(inbox.ref))
inbox.receiveAll() should be(empty)
```

Exception handling in Dsl.scala is as simple as ordinary Scala code, though it is difficult to be gratefully handled in Akka FSM API.

## Modules

`ReceiveMessage.Partial` accepts a type parameter, and it will skip messages whose types are not match the specified type parameter. To avoid the behavior of skipping message , use `ReceiveMessage` instead.

The above examples create some [typed actors](https://doc.akka.io/docs/akka/current/typed/actors.html) (i.e. `akka.actor.typed.Behavior`), but `ReceiveMessage.Partial` and `ReceiveMessage` supports [untyped actors](https://doc.akka.io/docs/akka/current/actors.html) as well. To create untyped actor from `Dsl.scala-akka-actor`, just change the return type from `akka.actor.typed.Behavior` to `akka.actor.Actor.Receive`.

These features are separated into two modules in this project:

### `receivemessage`

<a href="https://search.maven.org/search?q=g:com.yang-bo.dsl.keywords.akka.actor%20a:receivemessage_*"><img src="https://img.shields.io/maven-central/v/com.yang-bo.dsl.keywords.akka.actor/receivemessage_2.13.svg?label=libraryDependencies+%2B=+%22com.yang-bo.dsl.keywords.akka.actor%22+%25%25+%22receivemessage%22+%25"/></a> [![Scaladoc](https://javadoc.io/badge/com.yang-bo.dsl.keywords.akka.actor/receivemessage_2.13.svg?label=Documentation)](https://javadoc.io/page/com.yang-bo.dsl.keywords.akka.actor/receivemessage_2.13/latest/com/yang_bo/dsl/keywords/akka/actor/ReceiveMessage.html)

`receivemessage` library contains both `ReceiveMessage.Partial` and `ReceiveMessage`, to provide the direct style DSL to receive messages in Akka Actors.

### `typed`

<a href="https://search.maven.org/search?q=g:com.yang-bo.dsl.domains.akka.actor%20a:typed_*"><img src="https://img.shields.io/maven-central/v/com.yang-bo.dsl.domains.akka.actor/typed_2.13.svg?label=libraryDependencies+%2B=+%22com.yang-bo.dsl.domains.akka.actor%22+%25%25+%22typed%22+%25"/></a> [![Scaladoc](https://javadoc.io/badge/com.yang-bo.dsl.domains.akka.actor/typed_2.13.svg?label=Documentation)](https://javadoc.io/page/com.yang-bo.dsl.domains.akka.actor/typed_2.13/latest/com/yang_bo/dsl/domains/akka/actor/typed$.html)

`typed` enables the above direct style DSL in typed Akka Actors.

## Related projects

`!-notation` is a general notation in implemented in [Dsl.scala](https://github.com/ThoughtWorksInc/Dsl.scala/), for extracting the value from a keyword. This project is based on `Dsl.scala` and provides specific `ReceiveMessage.Partial` and `ReceiveMessage` keywords, which work in functions that return `Behavior`, `Receive` or curried functions that finally returns `Behavior` / `Receive`.

This project also supports [Scala.js](https://www.scala-js.org/), with the help of [Akka.js](http://akka-js.org/).

Examples in previous sections are written in [ScalaTest](http://www.scalatest.org/) and [ScalaMock](https://scalamock.org/).

## Links

* [Dsl.scala-akka-actor project page](https://github.com/Atry/Dsl.scala-akka-actor)
* [Dsl.scala-akka-actor API Documentation](https://javadoc.io/page/com.yang-bo.dsl.keywords.akka.actor/receivemessage_2.11/latest/com/yang_bo/dsl/domains/akka/actor/typed$.html)
* [Dsl.scala](https://github.com/ThoughtWorksInc/Dsl.scala/)
* [Akka](https://akka.io/)
* [Akka FSM](https://doc.akka.io/docs/akka/current/fsm.html)
* [Akka Actor](https://doc.akka.io/docs/akka/current/actors.html)
* [Akka Typed Actor](https://doc.akka.io/docs/akka/current/typed/actors.html)
* [Akka.js](http://akka-js.org/)
