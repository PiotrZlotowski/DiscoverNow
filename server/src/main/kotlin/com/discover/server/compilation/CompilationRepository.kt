package com.discover.server.compilation

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CompilationRepository: JpaRepository<Compilation, Long>