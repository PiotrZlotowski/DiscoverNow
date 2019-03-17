package com.discover.server.controller

import com.discover.server.domain.AuthenticationRequest
import com.discover.server.domain.AuthenticationToken
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.session.SessionAuthenticationException
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession
import javax.validation.Valid

@RestController
@RequestMapping("/api/authorization/")
class AuthenticationController constructor(private val authenticationManager: AuthenticationManager,
                                           private val userDetailsService: UserDetailsService) {

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(httpSession: HttpSession) {
        SecurityContextHolder.clearContext()
        httpSession.invalidate()
    }

    @PostMapping("/authenticate")
    fun authorize(@RequestBody @Valid authenticationRequestRequest: AuthenticationRequest,
                  httpServletRequest: HttpServletRequest): AuthenticationToken {
        val session = createHttpSession(authenticationRequestRequest, httpServletRequest)
                ?: throw SessionAuthenticationException("Unable to create user session!")
        return getAuthenticationToken(authenticationRequestRequest, session)
    }

    private fun getAuthenticationToken(authenticationRequestRequest: AuthenticationRequest, session: HttpSession): AuthenticationToken {
        val details = this.userDetailsService.loadUserByUsername(authenticationRequestRequest.username)
        val authToken = AuthenticationToken(username = details.username, sessionId = session.id)
        return authToken
    }

    private fun createHttpSession(authenticationRequestRequest: AuthenticationRequest, httpServletRequest: HttpServletRequest): HttpSession? {
        val token = UsernamePasswordAuthenticationToken(authenticationRequestRequest.username, authenticationRequestRequest.password)
        val authentication = this.authenticationManager.authenticate(token)
        SecurityContextHolder.getContext().authentication = authentication
        val session = httpServletRequest.getSession(true)
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext())
        return session
    }

}