package com.crownagentsbank.payments.axontesting.initiator

import com.crownagentsbank.payments.axontesting.InformationBEvent
import com.crownagentsbank.payments.axontesting.LocallyHandledEvent
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.TrackingToken
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("monolith", "initiator")
@Component
@ProcessingGroup("initiator-handlers")
class InitiatorEventHandler {

  private val logger = LoggerFactory.getLogger(InitiatorEventHandler::class.java)

  @EventHandler
  fun on(event: LocallyHandledEvent, token: TrackingToken) {
    logger.info("Handling locally handled event: $event (tracking token: $token")
  }

  //  @EventHandler
  //  fun on(event: AllInformationEvent) {
  //    logger.info("Handling all information event: $event")
  //  }

  //  @EventHandler
  //  fun on(event: InformationAEvent) {
  //    logger.info("Handling information A event: $event")
  //  }

  @EventHandler
  fun on(event: InformationBEvent) {
    logger.info("Handling information B event: $event")
  }
}
