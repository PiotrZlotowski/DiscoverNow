package com.discover.server.configuration

import com.discover.server.custom.spring.interceptor.ReloadUserInterceptor
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