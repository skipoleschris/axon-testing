package com.crownagentsbank.payments.axontesting

enum class CommandExperiments {
  local,
  remote,
  retry,
  exception
}

enum class QueryExperiments {
  local,
  remote,
  multiple,
  subscribe,
  stream
}

class BusinessException : Exception()

data class QueryResult(val id: Int, val details: String)
