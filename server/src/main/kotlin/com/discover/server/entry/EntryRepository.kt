package com.discover.server.entry

import com.discover.server.authentication.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EntryRepository: JpaRepository<Entry, Long> {

    fun findByIdAndAssociatedCompilation_User(id: Long, user: User): Entry?
}