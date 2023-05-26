package com.crownagentsbank.payments.axontesting.handler.basic

import com.crownagentsbank.payments.axontesting.RemotelyHandledCommand
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.config.ProcessingGroup
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("monolith", "basic1", "basic2")
@Component
@ProcessingGroup("simple-handler")
class CommandHandler {

  private val logger = LoggerFactory.getLogger(CommandHandler::class.java)

  @CommandHandler
  fun on(command: RemotelyHandledCommand) {
    logger.info("Handling command remotely: $command")
  }

  //    @EventHandler
  //    fun on(simpleEvent: SimpleEventHappenedEvent) {
  //        logger.info("Handling event: $simpleEvent")
  //    }
}
