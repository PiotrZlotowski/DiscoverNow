package com.discover.server.configuration

import com.discover.server.authentication.ReloadUserInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfiguration(private val reloadUserInterceptor: ReloadUserInterceptor): WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(reloadUserInterceptor).addPathPatterns("/api/**")
                .excludePathPatterns("/api/authorization/**")
    }

}