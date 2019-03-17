package com.discover.server.service

import com.discover.server.domain.compilation.Entry
import com.discover.server.exception.CompilationNotFoundException
import com.discover.server.repository.CompilationRepository
import com.discover.server.repository.EntryRepository
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