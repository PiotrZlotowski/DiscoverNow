package com.discover.server.compilation

import com.discover.server.authentication.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CompilationRepository : JpaRepository<Compilation, Long> {

    fun findByIdAndUser(id: Long, user: User): Compilation?

    fun deleteByIdAndUser(id: Long, user: User)
}