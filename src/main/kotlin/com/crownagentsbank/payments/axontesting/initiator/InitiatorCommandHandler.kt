package com.crownagentsbank.payments.axontesting.initiator

import com.crownagentsbank.payments.axontesting.CreateSimpleEventCommand
import com.crownagentsbank.payments.axontesting.LocallyHandledCommand
import com.crownagentsbank.payments.axontesting.SimpleEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventhandling.gateway.EventGateway
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("monolith", "initiator")
@Component
class InitiatorCommandHandler(private val eventGateway: EventGateway) {

  private val logger = LoggerFactory.getLogger(InitiatorCommandHandler::class.java)

  @CommandHandler
  fun on(command: LocallyHandledCommand) {
    logger.info("Handling command locally: $command")
  }

  @CommandHandler
  fun on(command: CreateSimpleEventCommand) {
    logger.info("Handling command to create a simple event: $command")
    eventGateway.publish(SimpleEvent(command.id))
  }
}
