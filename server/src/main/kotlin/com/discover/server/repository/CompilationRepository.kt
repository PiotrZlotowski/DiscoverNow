package com.discover.server.repository

import com.discover.server.domain.compilation.Compilation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CompilationRepository: JpaRepository<Compilation, Long>