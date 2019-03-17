package com.discover.server.custom.spring.interceptor

import com.discover.server.domain.user.User
import com.discover.server.service.UserService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ReloadUserInterceptor(private val userService: UserService): HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        // TODO: leci jeszcze jeden exception przy wykonywaniu requestów. Coś z not nullem
        val authentication = SecurityContextHolder.getContext().authentication

        authentication?.let {
            if (authentication !is UsernamePasswordAuthenticationToken) {
                return true
            }
            val user: User = authentication.principal as User
            val userName = userService.loadUserByUsername(user.username)
            val newAuth = UsernamePasswordAuthenticationToken(userName, userName.authorities, user.authorities)
            SecurityContextHolder.getContext().authentication = newAuth
        }

        return true
    }
}