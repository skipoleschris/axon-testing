package com.crownagentsbank.payments.axontesting.initiator

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
class TestController(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway
) {

  private val logger = LoggerFactory.getLogger(TestController::class.java)

  @GetMapping(value = ["/command/local/{id}"], produces = ["application/json"])
  @ResponseStatus(HttpStatus.ACCEPTED)
  fun sendLocallyHandledCommand(@PathVariable("id") id: Int): CompletableFuture<Any> {
    logger.info("Sending locally handled comment for id=$id")
    return commandGateway.send(LocallyHandledCommand(id))
  }

  @GetMapping(value = ["/command/remote/{id}"], produces = ["application/json"])
  @ResponseStatus(HttpStatus.ACCEPTED)
  fun sendRemotelyHandledCommand(@PathVariable("id") id: Int): CompletableFuture<Any> {
    logger.info("Sending remotely handled comment for id=$id")
    return commandGateway.send(RemotelyHandledCommand(id))
  }

  //    @GetMapping(value = [ "/send-command/event/{id}" ], produces = [ "application/json" ])
  //    @ResponseStatus(HttpStatus.ACCEPTED)
  //    fun sendCommandThatCausesAnEventFireAndForget(
  //        @PathVariable("id") id: Int
  //    ) {
  //        logger.info("Sending fire-and-forget EvenCreatingCommand for id=$id")
  //        commandGateway.send<Any>(EventCreatingCommand(id))
  //    }

  @GetMapping(value = ["/query/local/{id}"], produces = ["application/json"])
  @ResponseStatus(HttpStatus.OK)
  fun sendLocallyHandledQuery(@PathVariable("id") id: Int): CompletableFuture<QueryResult> {
    logger.info("Sending locally handled query for id=$id")
    return queryGateway.query(LocallyHandledQuery(id), QueryResult::class.java)
  }

  @GetMapping(value = ["/query/remote/{id}"], produces = ["application/json"])
  @ResponseStatus(HttpStatus.OK)
  fun sendRemotelyHandledQuery(@PathVariable("id") id: Int): CompletableFuture<QueryResult> {
    logger.info("Sending remotely handled query for id=$id")
    return queryGateway.query(RemotelyHandledQuery(id), QueryResult::class.java)
  }
}
