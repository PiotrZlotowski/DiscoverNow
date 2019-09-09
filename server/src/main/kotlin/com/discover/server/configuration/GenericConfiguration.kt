package com.discover.server.configuration

import org.h2.tools.Server
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.util.*

@Configuration
@EnableScheduling
class GenericConfiguration {

    @Bean
    fun restTemplate(restTemplateBuilder: RestTemplateBuilder): RestTemplate = restTemplateBuilder.build()

    @Bean
    fun localeResolver(): SessionLocaleResolver {
        val sessionLocaleResolver = SessionLocaleResolver()
        sessionLocaleResolver.setDefaultLocale(Locale.US)
        return sessionLocaleResolver
    }

    @Profile("dev")
    @Bean(initMethod = "start", destroyMethod = "stop")
    fun h2TcpServer() = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092")

}