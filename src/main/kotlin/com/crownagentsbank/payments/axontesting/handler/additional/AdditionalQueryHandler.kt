package com.crownagentsbank.payments.axontesting.handler.additional

import com.crownagentsbank.payments.axontesting.ScatterGatherQuery
import org.axonframework.queryhandling.QueryHandler
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("monolith", "additional")
@Component
class AdditionalQueryHandler(@Value("\${dataValues}") private val dataValues: List<String>) {

  private val logger = LoggerFactory.getLogger(AdditionalQueryHandler::class.java)

  @QueryHandler(queryName = "scatter-gather")
  fun on(query: ScatterGatherQuery): List<String> {
    logger.info("Handling scatter/gather query: $query, returning: $dataValues")
    return dataValues
  }
}
