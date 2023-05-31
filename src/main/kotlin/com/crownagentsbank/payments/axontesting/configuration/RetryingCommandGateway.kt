package com.crownagentsbank.payments.axontesting.configuration

import java.util.concurrent.CompletableFuture

interface RetryingCommandGateway {

  fun <R> send(command: Any): CompletableFuture<R>
}
