package com.crownagentsbank.payments.axontesting.initiator.web

import org.axonframework.commandhandling.NoHandlerForCommandException
import org.axonframework.queryhandling.NoHandlerForQueryException
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime

@Profile("monolith", "initiator")
@RestControllerAdvice
class WebExceptionHandler : ResponseEntityExceptionHandler() {

  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  @ExceptionHandler(NoHandlerForCommandException::class)
  fun handleCommandDispatchException(ex: NoHandlerForCommandException): ErrorModel =
      ErrorModel(HttpStatus.SERVICE_UNAVAILABLE, message = "No handler available for command")

  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  @ExceptionHandler(NoHandlerForQueryException::class)
  fun handleQueryDispatchException(ex: NoHandlerForQueryException): ErrorModel =
      ErrorModel(HttpStatus.SERVICE_UNAVAILABLE, message = "No handler available for query")
}

data class ErrorModel(
    val status: HttpStatus,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val message: String
)
