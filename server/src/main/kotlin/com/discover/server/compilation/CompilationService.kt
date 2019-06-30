package com.discover.server.compilation

import com.discover.server.authentication.User
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class CompilationService(private val compilationRepository: CompilationRepository) {

    fun getAllCompilations(user: User) = user.compilations

    @Transactional
    fun removeCompilation(compilationId: Long, user: User) = compilationRepository.deleteByIdAndUser(compilationId, user)

    fun getCompilation(compilationId: Long, user: User) = compilationRepository.findByIdAndUser(compilationId, user)

    fun addNewCompilation(compilation: Compilation, user: User, compilationType: CompilationType = CompilationType.USER_DEFINED): Compilation {
        compilation.user = user
        compilation.type = compilationType
        return compilationRepository.saveAndFlush(compilation)
    }

}