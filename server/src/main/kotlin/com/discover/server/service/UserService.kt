package com.discover.server.service

import com.discover.server.domain.User
import com.discover.server.repository.UserRepository
import org.springframework.context.annotation.Primary
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
@Primary
class UserService(private val userRepository: UserRepository): UserDetailsService {

    override fun loadUserByUsername(username: String): User {
        userRepository.findByEmail(username)?.let {
            return it
        }
        throw UsernameNotFoundException("Provided user name $username does not exist in the database")
    }
}