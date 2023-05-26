package com.crownagentsbank.payments.axontesting

data class LocallyHandledCommand(val id: Int)

data class RemotelyHandledCommand(val id: Int)

data class LocallyHandledQuery(val id: Int)

data class RemotelyHandledQuery(val id: Int)

data class QueryResult(val id: Int, val handlerLocation: String)

data class SimpleCommand(val id: Int)

data class EventCreatingCommand(val id: Int)

data class SimpleEventHappenedEvent(val id: Int)
