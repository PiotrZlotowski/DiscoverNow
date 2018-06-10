package com.discover.server.configuration

import org.springframework.beans.factory.annotation.Value
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
import javax.sql.DataSource

@EnableWebSecurity
class SecurityConfig(private val dataSource: DataSource) : WebSecurityConfigurerAdapter(false) {


    @Value("\${spring.security.queries.users}")
    private lateinit var usersQuery : String

    @Value("\${spring.security.queries.roles}")
    private lateinit var rolesQuery : String

    override fun configure(http: HttpSecurity) {
            http.authorizeRequests()
                    .antMatchers("/api/authorization/**").permitAll()
                    .antMatchers("/h2-console/**").permitAll()

            http.requestCache().requestCache(NullRequestCache())
            http.headers().frameOptions()?.sameOrigin()
            http.csrf().disable()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
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