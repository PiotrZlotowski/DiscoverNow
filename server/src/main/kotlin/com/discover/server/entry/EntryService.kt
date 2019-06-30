package com.discover.server.entry

import com.discover.server.authentication.User
import com.discover.server.compilation.EntityNotFoundException
import com.discover.server.compilation.CompilationService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class EntryService(private val entryRepository: EntryRepository,
                   private val compilationService: CompilationService) {


    fun findById(entryId: Long, user: User): Entry? = entryRepository.findByIdAndAssociatedCompilation_User(entryId, user)


    fun addNewEntry(compilationId: Long, user: User, entry: Entry): Entry {
        val compilation = compilationService.getCompilation(compilationId, user)
        compilation?.let {
            entry.associatedCompilation = compilation
            entry.timeCreated = LocalDateTime.now()
            return entryRepository.saveAndFlush(entry)
        }
        throw EntityNotFoundException(compilationId)
    }

    fun removeEntry(entryId: Long) = entryRepository.deleteById(entryId)

}