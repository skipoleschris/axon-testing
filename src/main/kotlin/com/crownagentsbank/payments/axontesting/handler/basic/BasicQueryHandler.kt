package com.crownagentsbank.payments.axontesting.handler.basic

import com.crownagentsbank.payments.axontesting.QueryResult
import com.crownagentsbank.payments.axontesting.RemotelyHandledQuery
import com.crownagentsbank.payments.axontesting.ScatterGatherQuery
import org.axonframework.queryhandling.QueryHandler
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("monolith", "basic1", "basic2")
@Component
class BasicQueryHandler(
    @Value("\${scatterGatherValues}") private val scatterGatherValues: List<String>
) {

  private val logger = LoggerFactory.getLogger(BasicQueryHandler::class.java)

  @QueryHandler
  fun on(query: RemotelyHandledQuery): QueryResult {
    logger.info("Handling query remotely: $query")
    return QueryResult(query.id, "remotely")
  }

  @QueryHandler(queryName = "scatter-gather")
  fun on(query: ScatterGatherQuery): List<String> {
    logger.info("Handling scatter/gather query: $query, returning: $scatterGatherValues")
    return scatterGatherValues
  }
}
