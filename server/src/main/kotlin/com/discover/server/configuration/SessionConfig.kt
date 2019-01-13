package com.discover.server.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer
import org.springframework.session.web.http.HeaderHttpSessionIdResolver

@Configuration
class SessionConfig : AbstractHttpSessionApplicationInitializer() {

    @Bean
    fun httpSessionIdResolver() : HeaderHttpSessionIdResolver = HeaderHttpSessionIdResolver.xAuthToken()
}