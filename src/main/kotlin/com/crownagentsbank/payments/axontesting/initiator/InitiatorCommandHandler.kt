package com.crownagentsbank.payments.axontesting.initiator

import com.crownagentsbank.payments.axontesting.*
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

  @CommandHandler
  fun on(command: CreateLocallyHandledEventCommand) {
    logger.info("Handling command to create a locally handled event: $command")
    eventGateway.publish(LocallyHandledEvent(command.id))
  }

  @CommandHandler
  fun on(command: CreateAllInformationEventCommand) {
    logger.info("Handling command to create all information event: $command")
    eventGateway.publish(AllInformationEvent(command.id, command.text))
  }
}
