package com.yang_bo.dsl.keywords.akka.actor

import akka.actor.Actor.Receive
import akka.actor.ActorContext
import com.thoughtworks.dsl.Dsl
import com.thoughtworks.dsl.Dsl.Keyword

import scala.reflect._

/** A [[com.thoughtworks.dsl.Dsl.Keyword]] to receive next message of an [[akka.actor.Actor]].
  *
  * @author 杨博 (Yang Bo)
  *
  * @example This [[NextMessage]] keyword can be used in the [[akka.actor.Actor.Receive]] domain,
  *          to receive the next message that is the [[com.yang_bo.dsl.keywords.akka.actor.NextMessage#messageClass message class]].
  *
  *          The above code creates an actor to echo any string messages.
  *
  *          {{{
  *          import com.yang_bo.dsl.keywords.akka.actor.NextMessage
  *          import akka.actor._
  *          val echo = system.actorOf(Props(new Actor {
  *            def receive: Receive = {
  *              while (true) {
  *                val lastMessage = !NextMessage[String]
  *                sender() ! lastMessage
  *              }
  *              throw new Exception("Unreachable code!")
  *            }
  *          }))
  *
  *          echo ! "hello world"
  *          expectMsg("hello world")
  *          }}}
  *
  *          All messages that are not [[messageClass]], which is [[String]] in the above case, will be skipped.
  *
  *          {{{
  *          echo ! 'SkippedMessage
  *          echo ! "string message"
  *          expectMsg("string message")
  *          }}}
  */
final case class NextMessage[Message](messageClass: Class[Message])
    extends AnyVal
    with Keyword[NextMessage[Message], Message]

object NextMessage {
  def apply[Message: ClassTag] = new NextMessage[Message](classTag[Message].runtimeClass.asInstanceOf[Class[Message]])

  implicit def nextMessageDsl[Message](implicit context: ActorContext): Dsl[NextMessage[Message], Receive, Message] = {
    (keyword, handler) => {
      case message if keyword.messageClass.isInstance(message) =>
        context.become(handler(message.asInstanceOf[Message]))
    }
  }
}
