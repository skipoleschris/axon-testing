package com.crownagentsbank.payments.axontesting.configuration

import org.axonframework.commandhandling.CommandBus
import org.axonframework.commandhandling.gateway.CommandGatewayFactory
import org.axonframework.commandhandling.gateway.IntervalRetryScheduler
import org.axonframework.common.jdbc.ConnectionProvider
import org.axonframework.eventhandling.tokenstore.TokenStore
import org.axonframework.eventhandling.tokenstore.jdbc.GenericTokenTableFactory
import org.axonframework.eventhandling.tokenstore.jdbc.JdbcTokenStore
import org.axonframework.serialization.Serializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.Executors

@Configuration
class AxonConfig(
    private val connectionProvider: ConnectionProvider,
    private val serializer: Serializer
) {

  @Bean
  fun tokenStore(): TokenStore {
    val store =
        JdbcTokenStore.builder()
            .connectionProvider(connectionProvider)
            .serializer(serializer)
            .build()

    store.createSchema(GenericTokenTableFactory.INSTANCE)
    return store
  }

  @Bean
  fun retryingCommandGateway(commandBus: CommandBus): RetryingCommandGateway {
    val executor = Executors.newScheduledThreadPool(1)
    val retryScheduler =
        IntervalRetryScheduler.builder()
            .retryInterval(6000)
            .maxRetryCount(10)
            .retryExecutor(executor)
            .build()
    val factory =
        CommandGatewayFactory.builder()
            .commandBus(commandBus)
            .retryScheduler(retryScheduler)
            .build()
    return factory.createGateway(RetryingCommandGateway::class.java)
  }
}
