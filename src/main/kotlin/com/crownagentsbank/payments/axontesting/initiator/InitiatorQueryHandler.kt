package com.crownagentsbank.payments.axontesting.initiator

import com.crownagentsbank.payments.axontesting.LocallyHandledQuery
import com.crownagentsbank.payments.axontesting.QueryResult
import org.axonframework.queryhandling.QueryHandler
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("monolith", "initiator")
@Component
class InitiatorQueryHandler {

  private val logger = LoggerFactory.getLogger(InitiatorQueryHandler::class.java)

  @QueryHandler
  fun on(query: LocallyHandledQuery): QueryResult {
    logger.info("Handling query locally: $query")
    return QueryResult(query.id, "locally")
  }
}
