package com.discover.server.facade

import com.discover.server.dto.SearchCriteria
import com.discover.server.dto.SourceRequest
import com.discover.server.model.Source
import com.discover.server.model.User
import com.discover.server.service.SearchService
import com.discover.server.service.SourceService
import ma.glasnost.orika.MapperFacade
import org.springframework.stereotype.Service

@Service
class SourceFacade(private val mapper: MapperFacade,
                   private val sourceService: SourceService,
                   private val searchService: SearchService<Source>) {

    fun addSource(sourceRequest: SourceRequest, user: User): SourceRequest {
        val source = mapper.map(sourceRequest, Source::class.java)
        val addedSource = sourceService.addSource(source, user)
        return mapper.map(addedSource, SourceRequest::class.java)
    }

    fun getSources() = sourceService.getSources()

    fun getSource(id: String) = sourceService.getSource(id)

    fun updateSource(id: String, sourceRequest: SourceRequest) {
        val source = mapper.map(sourceRequest, Source::class.java)
        sourceService.updateSource(id, source)
    }

    fun deleteSource(id: String) {
        sourceService.deleteSourceById(id)
    }

    fun findAll(searchCriteria: SearchCriteria): List<SourceRequest> {
        val specifications = searchCriteria.criteria.map { searchService.getSearchPredicate(it) }.toSet()
        val matchingSources = sourceService.findAll(specifications)
        return mapper.mapAsList(matchingSources, SourceRequest::class.java)
    }
}