package com.crownagentsbank.payments.axontesting.handler.basic

import com.crownagentsbank.payments.axontesting.QueryResult
import com.crownagentsbank.payments.axontesting.RemotelyHandledQuery
import org.axonframework.queryhandling.QueryHandler
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("monolith", "basic1", "basic2")
@Component
class BasicQueryHandler {

  private val logger = LoggerFactory.getLogger(BasicQueryHandler::class.java)

  @QueryHandler
  fun on(query: RemotelyHandledQuery): QueryResult {
    logger.info("Handling query remotely: $query")
    return QueryResult(query.id, "remotely")
  }
}
