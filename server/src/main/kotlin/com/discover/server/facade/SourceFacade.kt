package com.discover.server.facade

import com.discover.server.domain.SearchCriteria
import com.discover.server.domain.Source
import com.discover.server.domain.SourceDTO
import com.discover.server.domain.User
import com.discover.server.service.SearchService
import com.discover.server.service.SourceService
import ma.glasnost.orika.MapperFacade
import org.springframework.stereotype.Service

@Service
class SourceFacade(private val mapper: MapperFacade,
                   private val sourceService: SourceService,
                   private val searchService: SearchService<Source>) {

    fun addSource(sourceRequest: SourceDTO, user: User): SourceDTO {
        val source = mapper.map(sourceRequest, Source::class.java)
        val addedSource = sourceService.addSource(source, user)
        return mapper.map(addedSource, SourceDTO::class.java)
    }

    fun getSources() = sourceService.getSources()

    fun getSource(id: String) = sourceService.getSource(id)

    fun updateSource(id: String, sourceRequest: SourceDTO) {
        val source = mapper.map(sourceRequest, Source::class.java)
        sourceService.updateSource(id, source)
    }

    fun deleteSource(id: String) {
        sourceService.deleteSourceById(id)
    }

    fun findAll(searchCriteria: SearchCriteria): List<SourceDTO> {
        val specifications = searchCriteria.criteria.map { searchService.getSearchPredicate(it) }.toSet()
        val matchingSources = sourceService.findAll(specifications)
        return mapper.mapAsList(matchingSources, SourceDTO::class.java)
    }
}