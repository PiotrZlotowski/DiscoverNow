package com.discover.server.facade

import com.discover.server.dto.SearchCriteriaDTO
import com.discover.server.dto.SourceDTO
import com.discover.server.model.Source
import com.discover.server.service.SearchService
import com.discover.server.service.SourceService
import ma.glasnost.orika.MapperFacade
import org.springframework.stereotype.Service

@Service
class SourceFacade(private val mapper: MapperFacade,
                   private val sourceService: SourceService,
                   private val searchService: SearchService<Source>) {

    fun addSource(sourceDTO: SourceDTO): SourceDTO {
        val source = mapper.map(sourceDTO, Source::class.java)
        val addedSource = sourceService.addSource(source)
        return mapper.map(addedSource, SourceDTO::class.java)
    }

    fun getSources() = sourceService.getSources()

    fun getSource(id: String) = sourceService.getSource(id)

    fun updateSource(id: String, sourceDTO: SourceDTO) {
        val source = mapper.map(sourceDTO, Source::class.java)
        sourceService.updateSource(id, source)
    }

    fun deleteSource(id: String) {
        sourceService.deleteSourceById(id)
    }

    fun findAll(searchCriteriaDTO: SearchCriteriaDTO): List<SourceDTO> {
        val specifications = searchCriteriaDTO.searchCriteria.map { searchService.getSearchPredicate(it) }.toSet()
        val matchingSources = sourceService.findAll(specifications)
        return mapper.mapAsList(matchingSources, SourceDTO::class.java)
    }
}