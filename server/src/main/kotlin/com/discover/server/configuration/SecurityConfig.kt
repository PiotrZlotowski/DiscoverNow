package com.discover.server.configuration

import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.savedrequest.NullRequestCache

@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter(false) {

    override fun configure(http: HttpSecurity?) {
            http?.authorizeRequests()
                    ?.antMatchers("/api/authorization/**")?.permitAll()
                    ?.antMatchers("/h2-console/**")?.permitAll()

            http?.requestCache()?.requestCache(NullRequestCache())
            http?.csrf()?.disable()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.inMemoryAuthentication()?.withUser("admin")?.password("{noop}admin")?.roles("USER")
    }


    @Bean
    override fun userDetailsServiceBean(): UserDetailsService {
        return super.userDetailsServiceBean()
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
}