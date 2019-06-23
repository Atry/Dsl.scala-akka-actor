package com.yang_bo.dsl.keywords.akka.actor

import akka.actor.{Actor, ActorContext}
import com.thoughtworks.dsl.Dsl
import com.thoughtworks.dsl.Dsl.Keyword

import scala.reflect.ClassTag

/** A [[com.thoughtworks.dsl.Dsl.Keyword keyword]] to receive next message of an Akka actor.
  *
  * <a href="https://search.maven.org/search?q=g:com.yang-bo.dsl.keywords.akka.actor%20a:receivemessage_*"><img src="https://img.shields.io/maven-central/v/com.yang-bo.dsl.keywords.akka.actor/receivemessage_2.13.svg?label=libraryDependencies+%2B=+%22com.yang-bo.dsl.keywords.akka.actor%22+%25%25+%22receivemessage%22+%25"/></a>
  *
  * @tparam Message The type of message being received,
  *                 which must be [[Any]] for [[akka.actor.Actor]],
  *                 and must be the same type of `T` for [[akka.actor.typed.Behavior]].
  * @see [[domains.akka.actor.typed.typedReceiveMessageDsl]] for usage in the typed actor domains.
  * @author 杨博 (Yang Bo)
  * @example This [[ReceiveMessage]] keyword can be used in the [[akka.actor.Actor.Receive]] domain,
  *          to receive the next message.
  *
  *          The above code creates an actor to echo any messages.
  *
  *          {{{
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

  /** A [[com.thoughtworks.dsl.Dsl.Keyword keyword]] to receive next message of specific type in an Akka actor.
    *
    * @author 杨博 (Yang Bo)
    * @see [[domains.akka.actor.typed.typedReceiveMessagePartialDsl]] for usage in the typed actor domains.
    * @example This [[ReceiveMessage.Partial]] keyword can be used in the [[akka.actor.Actor.Receive]] domain,
    *          to receive the next message that is a [[Message]].
    *
    *          The above code creates an actor to echo any string messages.
    *
    *          {{{
    *          import akka.actor._
    *          def echoActor = new Actor {
    *            def receive: Receive = {
    *              while (true) {
    *                val lastMessage = !ReceiveMessage.Partial[String]
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
  final case class Partial[Message]() extends Keyword[ReceiveMessage.Partial[Message], Message]

  object Partial {
    implicit def receiveMessagePartialDsl[Message: ClassTag](
        implicit context: ActorContext
    ): Dsl[ReceiveMessage.Partial[Message], Actor.Receive, Message] = { (keyword, handler) =>
      {
        case message: Message =>
          context.become(handler(message))
      }
    }
  }

  implicit def receiveMessageDsl(
      implicit context: ActorContext
  ): Dsl[ReceiveMessage[Any], Actor.Receive, Any] = { (keyword, handler) =>
    {
      case message: Any =>
        context.become(handler(message))
    }
  }

}
