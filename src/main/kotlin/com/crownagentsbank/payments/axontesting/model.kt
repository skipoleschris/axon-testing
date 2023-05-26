package com.crownagentsbank.payments.axontesting

data class LocallyHandledCommand(val id: Int)

data class RemotelyHandledCommand(val id: Int)

data class LocallyHandledQuery(val id: Int)

data class RemotelyHandledQuery(val id: Int)

data class QueryResult(val id: Int, val handlerLocation: String)

data class CreateSimpleEventCommand(val id: Int)

data class SimpleEvent(val id: Int)
