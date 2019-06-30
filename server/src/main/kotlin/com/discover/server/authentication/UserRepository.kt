package com.discover.server.authentication

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository: JpaRepository<User, Long> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.sources LEFT JOIN FETCH u.compilations uc LEFT JOIN FETCH uc.entries uce LEFT JOIN FETCH uce.memos LEFT JOIN FETCH u.roles where u.email = ?1")
    fun findByEmail(email: String): User?

}