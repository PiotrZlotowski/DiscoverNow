package com.discover.server.repository

import com.discover.server.domain.compilation.Entry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EntryRepository: JpaRepository<Entry, Long>