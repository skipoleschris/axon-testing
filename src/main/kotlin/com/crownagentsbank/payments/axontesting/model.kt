package com.crownagentsbank.payments.axontesting

enum class CommandExperiments {
  local,
  remote,
  retry,
  exception
}

class BusinessException : Exception()
