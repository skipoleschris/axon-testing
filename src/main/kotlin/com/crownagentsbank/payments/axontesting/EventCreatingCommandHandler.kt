package com.crownagentsbank.payments.axontesting

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventhandling.gateway.EventGateway
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("initiator")
@Component
class EventCreatingCommandHandler(
    private val eventGateway: EventGateway
) {

    private val logger = LoggerFactory.getLogger(EventCreatingCommandHandler::class.java)

    @CommandHandler
    fun on(eventCreatingCommand: EventCreatingCommand) {
        logger.info("Handling command: $eventCreatingCommand")
        logger.info("Generating simple event id=${eventCreatingCommand.id}")
        eventGateway.publish(SimpleEventHappenedEvent(eventCreatingCommand.id))
    }
}