package com.discover.server.configuration

import com.discover.server.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.savedrequest.NullRequestCache

@EnableWebSecurity
class SecurityConfig(private val userService: UserService) : WebSecurityConfigurerAdapter(false) {


    override fun configure(http: HttpSecurity) {
            http.authorizeRequests()
                    .antMatchers("/api/authorization/**").permitAll()
                    .antMatchers("/h2-console/**").permitAll()
                    .antMatchers("/api/**").fullyAuthenticated()

            http.requestCache().requestCache(NullRequestCache())
            http.headers().frameOptions().sameOrigin()
            http.csrf().disable()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userService)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    override fun userDetailsServiceBean(): UserDetailsService {
        return userService
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
}