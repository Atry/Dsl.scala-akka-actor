package com.yang_bo.dsl.keywords.akka.actor

import akka.testkit.TestKitBase
import org.scalatest.{BeforeAndAfterAll, Suite}

/**
  * @author 杨博 (Yang Bo)
  */
trait ShutdownAfterAll extends BeforeAndAfterAll with TestKitBase { this: Suite =>
  override protected def afterAll(): Unit = {
    super.afterAll()
    shutdown()
  }
}
