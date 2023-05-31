package com.crownagentsbank.payments.axontesting.initiator

import com.crownagentsbank.payments.axontesting.*
import com.crownagentsbank.payments.axontesting.configuration.RetryingCommandGateway
import org.axonframework.commandhandling.CommandExecutionException
import org.axonframework.commandhandling.gateway.CommandGateway
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
class CommandExperimentsController(
    private val commandGateway: CommandGateway,
    private val retryingCommandGateway: RetryingCommandGateway,
    private val businessCommands: BusinessCommands
) {

  private val logger = LoggerFactory.getLogger(CommandExperimentsController::class.java)

  @GetMapping(value = ["/command/{experiment}/{id}"], produces = ["application/json"])
  @ResponseStatus(HttpStatus.ACCEPTED)
  fun sendCommand(
      @PathVariable("experiment") experiment: CommandExperiments,
      @PathVariable("id") id: Int
  ): CompletableFuture<Any> {
    logger.info("Command for id=$id, mode=$experiment")
    return when (experiment) {
      CommandExperiments.local -> localCommandExperiment(id)
      CommandExperiments.remote -> remoteCommandExperiment(id)
      CommandExperiments.retry -> retryCommandExperiment(id)
      CommandExperiments.exception -> exceptionCommandExperiment(id)
    }
  }

  private fun localCommandExperiment(id: Int): CompletableFuture<Any> =
      commandGateway.send<Any>(LocallyHandledCommand(id)).thenApply { "Command handled" }

  private fun remoteCommandExperiment(id: Int): CompletableFuture<Any> =
      commandGateway.send<Any>(RemotelyHandledCommand(id)).thenApply { "Command handled" }

  private fun retryCommandExperiment(id: Int): CompletableFuture<Any> =
      retryingCommandGateway.send<Any>(RemotelyHandledCommand(id)).thenApply { "Command handled" }

  private fun exceptionCommandExperiment(id: Int): CompletableFuture<Any> {
    val future = CompletableFuture<Any>()
    future.completeAsync {
      try {
        businessCommands.someBusinessProcess(BusinessExceptionCommand(id))
        "Business command completed successfully"
      } catch (ex: CommandExecutionException) {
        when (ex.message) {
          BusinessException::class.qualifiedName -> "Business exception that needs to be handled"
          else -> "Unknown business exception: ${ex.message}"
        }
      }
    }
    return future
  }
}
