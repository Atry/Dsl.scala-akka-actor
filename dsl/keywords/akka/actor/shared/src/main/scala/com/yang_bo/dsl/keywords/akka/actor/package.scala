package com.yang_bo.dsl.keywords.akka

import akka.actor.{Actor, ActorContext}
import com.thoughtworks.dsl.Dsl
import com.thoughtworks.dsl.Dsl.Keyword

import scala.reflect.ClassTag

/** Contains [[com.thoughtworks.dsl.Dsl.Keyword keyword]] for Akka actors.
  *
  * <a href="https://search.maven.org/search?q=g:com.yang-bo.dsl.keywords.akka%20a:actor_*"><img src="https://img.shields.io/maven-central/v/com.yang-bo.dsl.keywords.akka/actor_2.13.svg?label=libraryDependencies+%2B=+%22com.yang-bo.dsl.keywords.akka%22+%25%25+%22actor%22+%25"/></a>
  *
  * @author 杨博 (Yang Bo)
  */
package object actor
package actor {

  /** A [[com.thoughtworks.dsl.Dsl.Keyword keyword]] to receive next message of an Akka actor.
    *
    * @author 杨博 (Yang Bo)
    * @see [[domains.akka.actor.typed.typedReceiveMessagePartialDsl]] for usage in the typed actor domains.
    * @example This [[ReceiveMessagePartial]] keyword can be used in the [[akka.actor.Actor.Receive]] domain,
    *          to receive the next message that is a [[Message]].
    *
    *          The above code creates an actor to echo any string messages.
    *
    *          {{{
    *          import com.yang_bo.dsl.keywords.akka.actor.ReceiveMessagePartial
    *          import akka.actor._
    *          def echoActor = new Actor {
    *            def receive: Receive = {
    *              while (true) {
    *                val lastMessage = !ReceiveMessagePartial[String]
    *                sender() ! lastMessage
    *              }
    *              throw new Exception("Unreachable code!")
    *            }
    *          }
    *
    *          val pinger = system.actorOf(Props(echoActor))
    *          pinger ! "hello world"
    *          expectMsg("hello world")
    *          }}}
    *
    *          All messages that are not the class of [[Message]] will not be handled.
    *
    *          {{{
    *          object UnhandledMessage
    *          pinger ! UnhandledMessage
    *          pinger ! "string message"
    *          expectMsg("string message")
    *          }}}
    */
  final case class ReceiveMessagePartial[Message]() extends Keyword[ReceiveMessagePartial[Message], Message]

  object ReceiveMessagePartial {
    implicit def receiveMessagePartialDsl[Message: ClassTag](
        implicit context: ActorContext
    ): Dsl[ReceiveMessagePartial[Message], Actor.Receive, Message] = { (keyword, handler) =>
      {
        case message: Message =>
          context.become(handler(message))
      }
    }
  }

  /** A [[com.thoughtworks.dsl.Dsl.Keyword keyword]] to receive next message of an Akka actor.
    *
    * @tparam Message The type of message being received,
    *                 which must be [[Any]] for [[akka.actor.Actor]],
    *                 and must be the same type of `T` for [[akka.actor.typed.Behavior]].
    * @see [[domains.akka.actor.typed.typedReceiveMessageDsl]] for usage in the typed actor domains.
    *
    * @example This [[ReceiveMessage]] keyword can be used in the [[akka.actor.Actor.Receive]] domain,
    *          to receive the next message.
    *
    *          The above code creates an actor to echo any messages.
    *
    *          {{{
    *          import com.yang_bo.dsl.keywords.akka.actor.ReceiveMessage
    *          import akka.actor._
    *          def echoActor = new Actor {
    *            def receive: Receive = {
    *              while (true) {
    *                val lastMessage = !ReceiveMessage[Any]
    *                sender() ! lastMessage
    *              }
    *              throw new Exception("Unreachable code!")
    *            }
    *          }
    *
    *          val pinger = system.actorOf(Props(echoActor))
    *          pinger ! "hello world"
    *          expectMsg("hello world")
    *          }}}
    *
    *          All messages that are not the class of [[Message]] will not be handled.
    *
    *          {{{
    *          pinger ! "string message"
    *          expectMsg("string message")
    *          }}}
    */
  final case class ReceiveMessage[Message]() extends Keyword[ReceiveMessage[Message], Message]
  object ReceiveMessage {
    implicit def receiveMessageDsl(
        implicit context: ActorContext
    ): Dsl[ReceiveMessage[Any], Actor.Receive, Any] = { (keyword, handler) =>
      {
        case message: Any =>
          context.become(handler(message))
      }
    }

  }
}
