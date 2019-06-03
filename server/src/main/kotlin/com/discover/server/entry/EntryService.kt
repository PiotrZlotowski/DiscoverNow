package com.discover.server.entry

import com.discover.server.compilation.EntityNotFoundException
import com.discover.server.compilation.CompilationService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class EntryService(private val entryRepository: EntryRepository,
                   private val compilationService: CompilationService) {


    fun findById(entryId: Long): Entry? = entryRepository.findByIdOrNull(entryId)


    fun addNewEntry(compilationId: Long, entry: Entry): Entry {
        val compilation = compilationService.getCompilation(compilationId)
        compilation?.let {
            entry.associatedCompilation = compilation
            entry.timeCreated = LocalDateTime.now()
            return entryRepository.saveAndFlush(entry)
        }
        throw EntityNotFoundException(compilationId)
    }

    fun removeEntry(entryId: Long) = entryRepository.deleteById(entryId)

}