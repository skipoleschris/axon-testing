package com.crownagentsbank.payments.axontesting

import org.axonframework.common.jdbc.ConnectionProvider
import org.axonframework.eventhandling.tokenstore.TokenStore
import org.axonframework.eventhandling.tokenstore.jdbc.GenericTokenTableFactory
import org.axonframework.eventhandling.tokenstore.jdbc.JdbcTokenStore
import org.axonframework.serialization.Serializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

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
}
