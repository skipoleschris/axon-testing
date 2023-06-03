package com.crownagentsbank.payments.axontesting

data class LocallyHandledCommand(val id: Int)

data class RemotelyHandledCommand(val id: Int)

data class BusinessExceptionCommand(val id: Int)

data class LocallyHandledQuery(val id: Int)

data class RemotelyHandledQuery(val id: Int)

data class ScatterGatherQuery(val id: Int)

data class SubscriptionExperimentQuery(val id: Int)

data class StreamingExperimentQuery(val id: Int)

data class CreateSimpleEventCommand(val id: Int)

data class SimpleEvent(val id: Int)

data class CreateLocallyHandledEventCommand(val id: Int)

data class LocallyHandledEvent(val id: Int)
