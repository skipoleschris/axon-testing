package com.crownagentsbank.payments.axontesting.handler.basic

import com.crownagentsbank.payments.axontesting.BusinessException
import com.crownagentsbank.payments.axontesting.BusinessExceptionCommand
import com.crownagentsbank.payments.axontesting.RemotelyHandledCommand
import org.axonframework.commandhandling.CommandHandler
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("monolith", "basic1", "basic2")
@Component
class BasicCommandHandler {

  private val logger = LoggerFactory.getLogger(BasicCommandHandler::class.java)

  @CommandHandler
  fun on(command: RemotelyHandledCommand) {
    logger.info("Handling command remotely: $command")
  }

  @CommandHandler
  fun on(command: BusinessExceptionCommand) {
    logger.info("Remotely handing command and throwing a business exception: $command")
    throw BusinessException()
  }
}
