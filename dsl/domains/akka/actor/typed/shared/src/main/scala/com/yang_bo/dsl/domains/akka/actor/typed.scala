package com.yang_bo.dsl.domains.akka.actor

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.thoughtworks.dsl.Dsl
import com.yang_bo.dsl.keywords.akka.actor.ReceiveMessage, ReceiveMessage.Partial

import scala.language.higherKinds
import scala.reflect.ClassTag

/** Contains the [[com.thoughtworks.dsl.Dsl]] instances in a typed actor.
  *
  * == Installation ==
  *
  * This [[typed]] object supports [[keywords.akka.actor.ReceiveMessage.unary_$bang !]]-notation for [[keywords.akka.actor.ReceiveMessage]] in the typed actor domains,
  * which requires [[com.thoughtworks.dsl.compilerplugins.BangNotation BangNotation]] and [[com.thoughtworks.dsl.compilerplugins.ResetEverywhere ResetEverywhere]] compiler plugins along with this `typed` library.
  * For sbt, add the following settings to your `build.sbt`:
  *
  * <pre>
  * addCompilerPlugin("com.thoughtworks.dsl" %% "compilerplugins-bangnotation" % "latest.release")
  * addCompilerPlugin("com.thoughtworks.dsl" %% "compilerplugins-reseteverywhere" % "latest.release")
  * libraryDependencies += "com.yang-bo.dsl.domains.akka.actor" %% "typed" % "latest.release"
  * </pre>
  *
  * == Imports ==
  *
  * Then, add the following import statement to enable `ReceiveMessage` in the [[akka.actor.typed.Behavior]] domain.
  *
  * {{{
  * import com.yang_bo.dsl.domains.akka.actor.typed._
  * import com.yang_bo.dsl.keywords.akka.actor.ReceiveMessage
  * }}}
  *
  * @example This library can be used as an alternative to [[akka.actor.FSM]],
  *          for creating state machines in simple Scala control flow.
  *
  *          The following state machine contains two states and two transitions between them.
  *
  *          <br/><img src="https://upload.wikimedia.org/wikipedia/commons/thumb/c/cf/Finite_state_machine_example_with_comments.svg/420px-Finite_state_machine_example_with_comments.svg.png"/><br/>
  *
  *          It can be created as a simple `while` loop with the help of [[keywords.akka.actor.ReceiveMessage.Partial]]:
  *
  *          {{{
  *          import akka.actor.typed._
  *
  *          sealed trait State
  *          case object Opened extends State
  *          case object Closed extends State
  *
  *          sealed trait Transition
  *          case class Open(response: ActorRef[State]) extends Transition
  *          case class Close(response: ActorRef[State]) extends Transition
  *
  *          def doorActor: Behavior[Transition] = {
  *            while (true) {
  *              val open = !ReceiveMessage.Partial[Open]
  *              open.response ! Opened
  *              val close = !ReceiveMessage.Partial[Close]
  *              close.response ! Closed
  *            }
  *            throw new Exception("Unreachable code!")
  *          }
  *          }}}
  *
  *          The door should reply an `Opened` state after performing an `Open` transition,
  *
  *          {{{
  *          import akka.actor.testkit.typed.scaladsl._
  *          val door = BehaviorTestKit(doorActor)
  *          val state = TestInbox[State]()
  *          door.run(Open(state.ref))
  *          state.expectMessage(Opened)
  *          }}}
  *
  *          and the state of the door can be switched between `Opend` and `Closed` according to `Open` and `Close` transition.
  *
  *          {{{
  *          door.run(Close(state.ref))
  *          state.expectMessage(Closed)
  *          door.run(Open(state.ref))
  *          state.expectMessage(Opened)
  *          door.run(Close(state.ref))
  *          state.expectMessage(Closed)
  *          }}}
  * @note To use `try` / `catch` / `finally` expressions with !-notation,
  *       the return type of enclosing function should be `Behavior[?] !! Throwable`,
  *       as shown in the following `createDecoderActor` method.
  *
  *       It will open an [[java.io.InputStream]],
  *       read [[String]] from the stream,
  *       and close the stream in a `finally` block.
  *       
  *       {{{
  *       import akka.actor.typed._
  *       import akka.actor.typed.scaladsl._
  *       import com.thoughtworks.dsl.Dsl.!!
  *       import java.io._
  *       import java.net._
  *
  *       sealed trait Command
  *       case class Open(open: () => InputStream) extends Command
  *       case class ReadObject(response: ActorRef[String]) extends Command
  *       case object Close extends Command
  *
  *       def createDecoderActor: Behavior[Command] !! Throwable = {
  *         while (true) {
  *           val inputStream = (!ReceiveMessage.Partial[Open]).open()
  *           try {
  *             val ReadObject(replyTo) = !ReceiveMessage.Partial[ReadObject]
  *             replyTo ! new java.io.DataInputStream(inputStream).readUTF()
  *             !ReceiveMessage.Partial[Close.type]
  *           } finally {
  *             inputStream.close()
  *           }
  *         }
  *         throw new AssertionError("Unreachable code!")
  *       }
  *       }}}
  *
  *       Since `createDecoderActor` returns `Behavior[Command] !! Throwable`,
  *       it receives message of type `Command`,
  *       and accepts an additional callback function
  *       to handle exceptions that are not handled in `createDecoderActor`.
  *
  *       {{{
  *       import akka.actor.testkit.typed.scaladsl._
  *       val errorHandler = mockFunction[Throwable, Behavior[Command]]
  *       val decoderActor = BehaviorTestKit(createDecoderActor(errorHandler))
  *       }}}
  *
  *       Given an `InputStream` that throws an [[java.io.IOException]] when read from it,
  *
  *
  *       {{{
  *       val inputStream: InputStream = mock[InputStream]
  *       toMockFunction0(inputStream.read _).expects().throws(new IOException())
  *       decoderActor.run(Open(() => inputStream))
  *       }}}
  *
  *       when the `decoderActor` try to read a [[String]] from the stream,
  *       it should close the stream due to `finally` block triggered by the exception.
  *
  *       {{{
  *       val inbox = TestInbox[String]()
  *       errorHandler.expects(where[Throwable](_.isInstanceOf[IOException])).returns(Behaviors.stopped)
  *       toMockFunction0(inputStream.close _).expects().returns(()).once()
  *       decoderActor.run(ReadObject(inbox.ref))
  *       inbox.receiveAll() should be(empty)
  *       }}}
  * @author 杨博 (Yang Bo)
  */
object typed {

  /** Returns an [[com.thoughtworks.dsl.Dsl]] instance for [[keywords.akka.actor.ReceiveMessage]] in the [[akka.actor.typed.Behavior]] domain.
    *
    * @example Given an `echoActor` that receives `Ping` messages and replies corresponding `Pong` messages
    *
    *          {{{
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
    *          import akka.actor.testkit.typed.scaladsl._
    *          val pinger = BehaviorTestKit(echoActor)
    *          val probe = TestInbox[Pong]()
    *          pinger.run(Ping("hello", probe.ref))
    *          probe.expectMessage(Pong("hello"))
    *          }}}
    */
  implicit def typedReceiveMessageDsl[Message, Domain >: Behaviors.Receive[Message] <: Behavior[Message]]
    : Dsl[ReceiveMessage[Message], Domain, Message] = { (keyword, handler) =>
    Behaviors.receiveMessage[Message](handler)
  }

  /** Returns an [[com.thoughtworks.dsl.Dsl]] instance for [[keywords.akka.actor.ReceiveMessage.Partial]] in the [[akka.actor.typed.Behavior]] domain.
    *
    * @example Given an `echoActor` that receives `Ping` messages and replies corresponding `Pong` messages
    *
    *          {{{
    *          import akka.actor.typed._
    *
    *          case class Ping(message: String, response: ActorRef[Pong])
    *          case class Pong(message: String)
    *
    *          def echoActor: Behavior[AnyRef] = {
    *            while (true) {
    *              val Ping(m, replyTo) = !(ReceiveMessage.Partial[Ping])
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
    *          import akka.actor.testkit.typed.scaladsl._
    *          val pinger = BehaviorTestKit(echoActor)
    *          object UnhandledMessage
    *          pinger.run(UnhandledMessage)
    *          val probe = TestInbox[Pong]()
    *          pinger.run(Ping("hello", probe.ref))
    *          probe.expectMessage(Pong("hello"))
    *          }}}
    */
  implicit def typedReceiveMessagePartialDsl[PartialMessage <: Message,
                                             Message,
                                             Domain[T] >: Behaviors.Receive[T] <: Behavior[T]](
      implicit classTag: ClassTag[PartialMessage]
  ): Dsl[ReceiveMessage.Partial[PartialMessage], Domain[Message], PartialMessage] = { (keyword, handler) =>
    Behaviors.receiveMessagePartial[Message] {
      case message: PartialMessage =>
        handler(message)
    }
  }

}
