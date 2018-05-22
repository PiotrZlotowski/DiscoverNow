package com.discover.server.controller

import com.discover.server.dto.AuthenticationRequest
import com.discover.server.dto.AuthenticationToken
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.web.bind.annotation.*
import java.util.ArrayList
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("/api/authorization/")
class AuthenticationController constructor(private val authenticationManager : AuthenticationManager,
                                           private val userDetailsService : UserDetailsService) {



    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(httpSession: HttpSession) {
        httpSession.invalidate()
    }

    @PostMapping("/authenticate")
    fun authorize(@RequestBody authenticationRequestRequest: AuthenticationRequest,
                  httpServletRequest: HttpServletRequest) : AuthenticationToken {
        val token = UsernamePasswordAuthenticationToken(authenticationRequestRequest.username, authenticationRequestRequest.password)
        val authentication = this.authenticationManager.authenticate(token)
        SecurityContextHolder.getContext().authentication = authentication
        val session = httpServletRequest.getSession(true)
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext())

        val details = this.userDetailsService.loadUserByUsername(authenticationRequestRequest.username)
        val roles = ArrayList<String>()
        for (authority in details.authorities) {
            roles.add(authority.toString())
        }

        val authToken = AuthenticationToken(username = details.username, sessionId = session.id)
        return authToken
    }

}