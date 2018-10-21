package com.discover.server.configuration

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableScheduling
class GenericConfiguration {

    @Bean
    fun restTemplate(restTemplateBuilder: RestTemplateBuilder) = restTemplateBuilder.build()

}