package com.crownagentsbank.payments.axontesting.initiator.web

import com.crownagentsbank.payments.axontesting.*
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.concurrent.CompletableFuture

@Profile("monolith", "initiator")
@RestController
@RequestMapping("/axon-test")
@Validated
class InProgressExperimentsController(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway
) {

  private val logger = LoggerFactory.getLogger(InProgressExperimentsController::class.java)

  @GetMapping(value = ["/event/local/{id}"], produces = ["application/json"])
  @ResponseStatus(HttpStatus.ACCEPTED)
  fun generateLocallyHandledEvent(@PathVariable("id") id: Int): CompletableFuture<Any> {
    logger.info("Sending command to generate a locally handled event for id=$id")
    return commandGateway.send(CreateLocallyHandledEventCommand(id))
  }

  @GetMapping(value = ["/event/simple/{id}"], produces = ["application/json"])
  @ResponseStatus(HttpStatus.ACCEPTED)
  fun generateSimpleEvent(@PathVariable("id") id: Int): CompletableFuture<Any> {
    logger.info("Sending command to generate a simple event for id=$id")
    return commandGateway.send(CreateSimpleEventCommand(id))
  }

  @GetMapping(value = ["/event/allinfo/{id}/{text}"], produces = ["application/json"])
  @ResponseStatus(HttpStatus.ACCEPTED)
  fun generateAllInformationEvent(
      @PathVariable("id") id: Int,
      @PathVariable("text") text: String
  ): CompletableFuture<Any> {
    logger.info("Sending command to generate a all information event for id=$id, with text=$text")
    return commandGateway.send(CreateAllInformationEventCommand(id, text))
  }
}
