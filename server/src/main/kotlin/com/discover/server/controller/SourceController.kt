package com.discover.server.controller

import com.discover.server.dto.SearchCriteria
import com.discover.server.dto.SourceRequest
import com.discover.server.facade.SourceFacade
import com.discover.server.model.Source
import com.discover.server.model.User
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/sources")
class SourceController(private val sourceFacade: SourceFacade) {

    @PostMapping
    fun addSource(@Valid @RequestBody source: SourceRequest, @AuthenticationPrincipal user: User) = sourceFacade.addSource(source, user)

    @GetMapping
    fun getSources(): Iterable<Source> = sourceFacade.getSources()

    @GetMapping("/{id}")
    fun getSource(@PathVariable("id") id: String) = sourceFacade.getSource(id)

    @PutMapping("/{id}")
    fun updateSource(@PathVariable("id") id: String, @Valid @RequestBody source: SourceRequest) {
            sourceFacade.updateSource(id, source)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteSource(@PathVariable("id") id: String) {
        sourceFacade.deleteSource(id)
    }
}

@RestController
@RequestMapping("/api/source-searches")
class SourceSearchController(private val sourceFacade: SourceFacade) {

    @PostMapping
    fun searchSources(@RequestBody searchCriteria: SearchCriteria): List<SourceRequest> {
        return sourceFacade.findAll(searchCriteria)
    }
}