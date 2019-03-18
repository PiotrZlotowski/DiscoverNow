package com.discover.server.entry

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EntryRepository: JpaRepository<Entry, Long>