package com.crownagentsbank.payments.axontesting

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AxonTesting

fun main(args: Array<String>) {
    runApplication<AxonTesting>(*args)
}