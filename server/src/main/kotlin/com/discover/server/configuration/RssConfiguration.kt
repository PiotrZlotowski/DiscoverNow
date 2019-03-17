package com.discover.server.configuration

import com.rometools.rome.io.SyndFeedInput
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RssConfiguration {

    @Bean
    fun syndFeedInput() : SyndFeedInput = SyndFeedInput()
}