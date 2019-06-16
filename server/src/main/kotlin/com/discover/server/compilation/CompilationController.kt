package com.discover.server.compilation

import com.discover.server.entry.CreateEntryRequest
import com.discover.server.authentication.User
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
    fun removeCollection(@PathVariable compilationId: Long, @AuthenticationPrincipal user: User) = compilationFacade.removeCollection(compilationId, user)

    @GetMapping("{compilationId}")
    fun getCompilation(@PathVariable compilationId: Long, @AuthenticationPrincipal user: User) = compilationFacade.getCompilation(compilationId, user)


    @PostMapping("{compilationId}/entries")
    fun addNewEntry(@PathVariable compilationId: Long, @AuthenticationPrincipal user: User, @RequestBody entry: CreateEntryRequest) = compilationFacade.addNewEntry(compilationId, user, entry)


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/entries/{entryId}")
    fun removeEntry(@PathVariable entryId: Long) {
        compilationFacade.removeEntry(entryId)
    }

}