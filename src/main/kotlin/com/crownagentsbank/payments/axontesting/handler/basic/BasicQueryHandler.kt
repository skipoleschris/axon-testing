package com.crownagentsbank.payments.axontesting.handler.basic

import com.crownagentsbank.payments.axontesting.QueryResult
import com.crownagentsbank.payments.axontesting.RemotelyHandledQuery
import com.crownagentsbank.payments.axontesting.ScatterGatherQuery
import com.crownagentsbank.payments.axontesting.SubscriptionExperimentQuery
import org.axonframework.queryhandling.QueryHandler
import org.axonframework.queryhandling.QueryUpdateEmitter
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("monolith", "basic1", "basic2")
@Component
class BasicQueryHandler(
    @Value("\${dataValues}") private val dataValues: List<String>,
    private val queryUpdateEmitter: QueryUpdateEmitter
) {

  private val logger = LoggerFactory.getLogger(BasicQueryHandler::class.java)

  @QueryHandler
  fun on(query: RemotelyHandledQuery): QueryResult {
    logger.info("Handling query remotely: $query")
    return QueryResult(query.id, "remotely")
  }

  @QueryHandler(queryName = "scatter-gather")
  fun on(query: ScatterGatherQuery): List<String> {
    logger.info("Handling scatter/gather query: $query, returning: $dataValues")
    return dataValues
  }

  @QueryHandler(queryName = "subscription")
  fun on(query: SubscriptionExperimentQuery): List<String> {
    logger.info("Handing initial subscription query result: $query, returning: $dataValues")
    processUpdates(query)
    return dataValues
  }

  private fun processUpdates(query: SubscriptionExperimentQuery) {
    Thread {
          logger.info("Starting update thread: $query ...")
          dataValues.forEach {
            Thread.sleep(2000L)
            logger.info("Emitting update: $query, value: $it")
            queryUpdateEmitter.emit(SubscriptionExperimentQuery::class.java, { true }, it)
          }
          queryUpdateEmitter.complete { true }
          logger.info("Completed update thread: $query")
        }
        .start()
  }
}
