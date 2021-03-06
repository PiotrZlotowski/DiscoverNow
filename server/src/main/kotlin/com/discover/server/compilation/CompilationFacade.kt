package com.discover.server.compilation

import com.discover.server.entry.CreateEntryRequest
import com.discover.server.entry.Entry
import com.discover.server.entry.EntryDTO
import com.discover.server.authentication.User
import com.discover.server.common.annotation.Facade
import com.discover.server.entry.EntryService
import ma.glasnost.orika.MapperFacade


@Facade
class CompilationFacade(private val compilationService: CompilationService,
                        private val entryService: EntryService,
                        private val mapper: MapperFacade) {

    fun getAllCollections(user: User): List<CompilationDTO> {
        val userCompilations = compilationService.getAllCompilations(user)
        return mapper.mapAsList(userCompilations, CompilationDTO::class.java)
    }

    fun removeCollection(collectionId: Long, user: User) {
        compilationService.removeCompilation(collectionId, user)
    }

    fun getCompilation(compilationId: Long, user: User): CompilationDTO {
        val compilation = compilationService.getCompilation(compilationId, user)
        return mapper.map(compilation, CompilationDTO::class.java)
    }

    fun addNewEntry(compilationId: Long, user: User, newEntry: CreateEntryRequest): EntryDTO {
        val entry = mapper.map(newEntry, Entry::class.java)
        val persistedEntry = entryService.addNewEntry(compilationId, user, entry)
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