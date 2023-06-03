package com.crownagentsbank.payments.axontesting.initiator.web

import com.crownagentsbank.payments.axontesting.*
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import kotlin.streams.asSequence

@Profile("monolith", "initiator")
@RestController
@RequestMapping("/axon-test")
@Validated
class QueryExperimentsController(private val queryGateway: QueryGateway) {

  private val logger = LoggerFactory.getLogger(QueryExperimentsController::class.java)

  @GetMapping(value = ["/query/{experiment}/{id}"], produces = ["application/json"])
  @ResponseStatus(HttpStatus.OK)
  fun launchExperiment(
      @PathVariable("experiment") experiment: QueryExperiments,
      @PathVariable("id") id: Int
  ): CompletableFuture<QueryResult> {
    logger.info("Query for id=$id, mode=$experiment")
    return when (experiment) {
      QueryExperiments.local -> localQueryExperiment(id)
      QueryExperiments.remote -> remoteQueryExperiment(id)
      QueryExperiments.multiple -> multipleQueryExperiment(id)
      QueryExperiments.subscribe -> subscribeQueryExperiment(id)
      QueryExperiments.stream -> streamQueryExperiment(id)
    }
  }

  private fun localQueryExperiment(id: Int): CompletableFuture<QueryResult> =
      queryGateway.query(LocallyHandledQuery(id), QueryResult::class.java)

  private fun remoteQueryExperiment(id: Int): CompletableFuture<QueryResult> =
      queryGateway.query(RemotelyHandledQuery(id), QueryResult::class.java)

  private fun multipleQueryExperiment(id: Int): CompletableFuture<QueryResult> {
    val future = CompletableFuture<QueryResult>()
    future.completeAsync {
      val result =
          queryGateway
              .scatterGather(
                  "scatter-gather",
                  ScatterGatherQuery(id),
                  ResponseTypes.multipleInstancesOf(String::class.java),
                  10L,
                  TimeUnit.SECONDS)
              .asSequence()
              .flatMap { it }
              .joinToString()
      QueryResult(id, result)
    }
    return future
  }

  private fun subscribeQueryExperiment(id: Int): CompletableFuture<QueryResult> =
      CompletableFuture.supplyAsync {
        val items = mutableListOf<String>()
        val result =
            queryGateway.subscriptionQuery(
                "subscription",
                SubscriptionExperimentQuery(id),
                ResponseTypes.multipleInstancesOf(String::class.java),
                ResponseTypes.instanceOf(String::class.java))

        result.handle(
            {
              logger.info("Initial results: $it")
              items.addAll(it)
            },
            {
              logger.info("Update result: $it")
              items.add(it)
            })

        Thread.sleep(10000L)
        result.close()
        QueryResult(id, items.joinToString())
      }

  private fun streamQueryExperiment(id: Int): CompletableFuture<QueryResult> =
      CompletableFuture.supplyAsync {
        val items = mutableListOf<String>()
        Flux.from(queryGateway.streamingQuery(StreamingExperimentQuery(id), String::class.java))
            .log()
            .takeUntil { it == "END" }
            .doOnNext {
              logger.info("Received streamed value: $it")
              items.add(it)
            }
            .subscribe()

        Thread.sleep(2000L)
        QueryResult(id, items.joinToString())
      }
}
