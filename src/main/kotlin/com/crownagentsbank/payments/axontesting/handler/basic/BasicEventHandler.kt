package com.crownagentsbank.payments.axontesting.handler.basic

import com.crownagentsbank.payments.axontesting.SimpleEvent
import org.axonframework.eventhandling.EventHandler
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("monolith", "basic1", "basic2")
@Component
class BasicEventHandler {

  private val logger = LoggerFactory.getLogger(BasicEventHandler::class.java)

  @EventHandler
  fun on(event: SimpleEvent) {
    logger.info("Handling simple event: $event")
  }
}
