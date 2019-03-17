package com.discover.server.controller

import com.discover.server.domain.compilation.CreateCompilationRequest
import com.discover.server.domain.compilation.CreateEntryRequest
import com.discover.server.domain.user.User
import com.discover.server.facade.CompilationFacade
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/compilations")
class CompilationController(private val compilationFacade: CompilationFacade) {

    @GetMapping("/")
    fun getAllCompilations(@AuthenticationPrincipal user: User) = compilationFacade.getAllCollections(user)


    @PostMapping("/")
    fun addNewCompilation(@RequestBody createCompilationRequest: CreateCompilationRequest, @AuthenticationPrincipal user: User) = compilationFacade.addNewCompilation(createCompilationRequest, user)

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{compilationId}")
    fun removeCollection(@PathVariable compilationId: Long) = compilationFacade.removeCollection(compilationId)

    @GetMapping("{compilationId}")
    fun getCompilation(@PathVariable compilationId: Long) = compilationFacade.getCompilation(compilationId)


    @PostMapping("{compilationId}/entries")
    fun addNewEntry(@PathVariable compilationId: Long, @RequestBody entry: CreateEntryRequest) = compilationFacade.addNewEntry(compilationId, entry)


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/entries/{entryId}")
    fun removeEntry(@PathVariable entryId: Long) {
        compilationFacade.removeEntry(entryId)
    }

}