package com.discover.server.entry

import com.discover.server.compilation.CompilationNotFoundException
import com.discover.server.compilation.CompilationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class EntryService(private val entryRepository: EntryRepository,
                   private val compilationRepository: CompilationRepository) {


    fun addNewEntry(compilationId: Long, entry: Entry): Entry {
        val compilation = compilationRepository.findByIdOrNull(compilationId)
        compilation?.let {
            entry.associatedCompilation = compilation
            entry.timeCreated = LocalDateTime.now()
            return entryRepository.saveAndFlush(entry)
        }
        throw CompilationNotFoundException(compilationId)
    }

    fun removeEntry(entryId: Long) = entryRepository.deleteById(entryId)

}