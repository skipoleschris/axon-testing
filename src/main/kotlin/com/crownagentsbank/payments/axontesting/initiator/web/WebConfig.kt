package com.crownagentsbank.payments.axontesting.initiator.web

import com.crownagentsbank.payments.axontesting.CommandExperiments
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.convert.converter.Converter
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Profile("monolith", "initiator")
@Configuration
class WebConfig : WebMvcConfigurer {
  override fun addFormatters(registry: FormatterRegistry) {
    registry.addConverter(CommandExperimentsConverter())
  }
}

class CommandExperimentsConverter : Converter<String, CommandExperiments> {
  override fun convert(s: String) = CommandExperiments.valueOf(s)
}
