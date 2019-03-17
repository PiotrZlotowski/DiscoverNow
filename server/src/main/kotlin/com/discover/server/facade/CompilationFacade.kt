package com.discover.server.facade

import com.discover.server.domain.compilation.Compilation
import com.discover.server.domain.compilation.CompilationDTO
import com.discover.server.domain.compilation.CreateCompilationRequest
import com.discover.server.domain.compilation.CreateEntryRequest
import com.discover.server.domain.compilation.Entry
import com.discover.server.domain.compilation.EntryDTO
import com.discover.server.domain.user.User
import com.discover.server.service.CompilationService
import com.discover.server.service.EntryService
import ma.glasnost.orika.MapperFacade
import org.springframework.stereotype.Service


@Service
class CompilationFacade(private val compilationService: CompilationService,
                        private val entryService: EntryService,
                        private val mapper: MapperFacade) {

    fun getAllCollections(user: User): List<CompilationDTO> {
        val userCompilations = compilationService.getAllCompilations(user)
        return mapper.mapAsList(userCompilations, CompilationDTO::class.java)
    }

    fun removeCollection(collectionId: Long) {
        compilationService.removeCompilation(collectionId)
    }

    fun getCompilation(compilationId: Long): CompilationDTO  {
        val compilation = compilationService.getCompilation(compilationId)
        return mapper.map(compilation, CompilationDTO::class.java)
    }

    fun addNewEntry(compilationId: Long, newEntry: CreateEntryRequest): EntryDTO {
        val entry = mapper.map(newEntry, Entry::class.java)
        val persistedEntry = entryService.addNewEntry(compilationId, entry)
        return mapper.map(persistedEntry, EntryDTO::class.java)

    }

    fun removeEntry(entryId: Long) {
        entryService.removeEntry(entryId)
    }

    fun addNewCompilation(createCompilationRequest: CreateCompilationRequest, user: User): CompilationDTO {
        val compilation = mapper.map(createCompilationRequest, Compilation::class.java)
        val persistedCompilation = compilationService.addNewCompilation(compilation, user)
        return mapper.map(persistedCompilation, CompilationDTO::class.java)
    }


}