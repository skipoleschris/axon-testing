package com.crownagentsbank.payments.axontesting.initiator

import com.crownagentsbank.payments.axontesting.BusinessException
import com.crownagentsbank.payments.axontesting.BusinessExceptionCommand

interface BusinessCommands {

  @Throws(BusinessException::class) fun someBusinessProcess(command: BusinessExceptionCommand)
}
