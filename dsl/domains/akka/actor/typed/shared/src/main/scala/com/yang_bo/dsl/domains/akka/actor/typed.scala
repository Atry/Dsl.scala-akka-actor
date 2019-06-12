package com.yang_bo.dsl.domains.akka.actor

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.thoughtworks.dsl.Dsl
import com.yang_bo.dsl.keywords.akka.actor.{ReceiveMessage, ReceiveMessagePartial}

import scala.language.higherKinds
import scala.reflect.ClassTag

/** Contains the [[com.thoughtworks.dsl.Dsl]] instances in the [[akka.actor.typed.Behavior]] domain.
  *
  * <a href="https://search.maven.org/search?q=g:com.yang-bo.dsl.domains.akka.actor%20a:typed_*"><img src="https://img.shields.io/maven-central/v/com.yang-bo.dsl.domains.akka.actor/typed_2.13.svg?label=libraryDependencies+%2B=+%22com.yang-bo.dsl.domains.akka.actor%22+%25%25+%22typed%22+%25"/></a>
  *
  * @author 杨博 (Yang Bo)
  */
object typed {

  /** Returns an [[com.thoughtworks.dsl.Dsl]] instance for [[keywords.akka.actor.ReceiveMessage]] in the [[akka.actor.typed.Behavior]] domain.
    *
    * @example Given an `echoActor` that receives `Ping` messages and replies corresponding `Pong` messages
    *
    *          {{{
    *          import com.yang_bo.dsl.keywords.akka.actor.ReceiveMessage
    *          import com.yang_bo.dsl.domains.akka.actor.typed.typedReceiveMessageDsl
    *          import akka.actor.typed._
    *
    *          case class Ping(message: String, response: ActorRef[Pong])
    *          case class Pong(message: String)
    *
    *          def echoActor: Behavior[Ping] = {
    *            while (true) {
    *              val Ping(m, replyTo) = !ReceiveMessage[Ping]
    *              replyTo ! Pong(m)
    *            }
    *            throw new Exception("Unreachable code!")
    *          }
    *          }}}
    *
    *          When pinging it with "hello",
    *          then it should reply a `Pong` with the same message "hello".
    *
    *          {{{
    *          import akka.actor.testkit.typed.scaladsl.ActorTestKit
    *          val testKit = ActorTestKit()
    *          try {
    *            val pinger = testKit.spawn(echoActor, "ping")
    *            val probe = testKit.createTestProbe[Pong]()
    *            pinger ! Ping("hello", probe.ref)
    *            probe.expectMessage(Pong("hello"))
    *          } finally {
    *            testKit.shutdownTestKit()
    *          }
    *          }}}
    */
  implicit def typedReceiveMessageDsl[Message, Domain >: Behaviors.Receive[Message] <: Behavior[Message]]
    : Dsl[ReceiveMessage[Message], Domain, Message] = { (keyword, handler) =>
    Behaviors.receiveMessage[Message](handler)
  }

  /** Returns an [[com.thoughtworks.dsl.Dsl]] instance for [[keywords.akka.actor.ReceiveMessagePartial]] in the [[akka.actor.typed.Behavior]] domain.
    *
    * @example Given an `echoActor` that receives `Ping` messages and replies corresponding `Pong` messages
    *
    *          {{{
    *          import com.yang_bo.dsl.keywords.akka.actor.ReceiveMessagePartial
    *          import com.yang_bo.dsl.domains.akka.actor.typed.typedReceiveMessagePartialDsl
    *          import akka.actor.typed._
    *
    *          case class Ping(message: String, response: ActorRef[Pong])
    *          case class Pong(message: String)
    *
    *          def echoActor: Behavior[AnyRef] = {
    *            while (true) {
    *              val Ping(m, replyTo) = !ReceiveMessagePartial[Ping]
    *              replyTo ! Pong(m)
    *            }
    *            throw new Exception("Unreachable code!")
    *          }
    *          }}}
    *
    *          When pinging it with "hello",
    *          then it should reply a `Pong` with the same message "message",
    *          and other types of messages should not be handled.
    *
    *          {{{
    *          import akka.actor.testkit.typed.scaladsl.ActorTestKit
    *          val testKit = ActorTestKit()
    *          try {
    *            val pinger = testKit.spawn(echoActor, "ping")
    *            val probe = testKit.createTestProbe[Pong]()
    *            object UnhandledMessage
    *            pinger ! UnhandledMessage
    *            pinger ! Ping("hello", probe.ref)
    *            probe.expectMessage(Pong("hello"))
    *          } finally {
    *            testKit.shutdownTestKit()
    *          }
    *          }}}
    */
  implicit def typedReceiveMessagePartialDsl[PartialMessage <: Message,
                                             Message,
                                             Domain[T] >: Behaviors.Receive[T] <: Behavior[T]](
      implicit classTag: ClassTag[PartialMessage]
  ): Dsl[ReceiveMessagePartial[PartialMessage], Domain[Message], PartialMessage] = { (keyword, handler) =>
    Behaviors.receiveMessagePartial[Message] {
      case message: PartialMessage =>
        handler(message)
    }
  }

}
