package com.discover.server.compilation

import com.discover.server.authentication.User
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