package com.discover.server.service

import com.discover.server.domain.compilation.Compilation
import com.discover.server.domain.compilation.CompilationType
import com.discover.server.domain.user.User
import com.discover.server.repository.CompilationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CompilationService(private val compilationRepository: CompilationRepository) {

    fun getAllCompilations(user: User) = user.compilations

    fun removeCompilation(compilationId: Long) = compilationRepository.deleteById(compilationId)

    fun getCompilation(compilationId: Long) = compilationRepository.findByIdOrNull(compilationId)

    fun addNewCompilation(compilation: Compilation, user: User, compilationType: CompilationType = CompilationType.USER_DEFINED): Compilation {
        compilation.user = user
        compilation.type = compilationType
        return compilationRepository.saveAndFlush(compilation)
    }

}