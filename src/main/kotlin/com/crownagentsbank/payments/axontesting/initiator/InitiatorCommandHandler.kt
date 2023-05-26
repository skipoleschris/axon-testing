package com.crownagentsbank.payments.axontesting.initiator

import com.crownagentsbank.payments.axontesting.LocallyHandledCommand
import org.axonframework.commandhandling.CommandHandler
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("monolith", "initiator")
@Component
class InitiatorCommandHandler {

  private val logger = LoggerFactory.getLogger(CommandHandler::class.java)

  @CommandHandler
  fun on(command: LocallyHandledCommand) {
    logger.info("Handling command locally: $command")
  }
}
