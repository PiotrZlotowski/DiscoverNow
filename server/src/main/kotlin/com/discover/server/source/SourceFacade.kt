package com.discover.server.source

import com.discover.server.search.SearchCriteria
import com.discover.server.authentication.User
import com.discover.server.common.annotation.Facade
import com.discover.server.feed.RssFeedService
import com.discover.server.search.SearchService
import ma.glasnost.orika.MapperFacade

@Facade
class SourceFacade(private val mapper: MapperFacade,
                   private val sourceService: SourceService,
                   private val searchService: SearchService<Source>,
                   private val rssFeedService: RssFeedService) {

    fun addSource(sourceRequest: SourceDTO, user: User): SourceDTO {
        val isValidFeed = rssFeedService.isValidFeed(sourceRequest.url)
        if (!isValidFeed) {
            throw InvalidSourceFormatException(sourceRequest.url)
        }
        val source = mapper.map(sourceRequest, Source::class.java)
        val addedSource = sourceService.addSource(source, user)
        return mapper.map(addedSource, SourceDTO::class.java)
    }

    fun getSources(): List<SourceDTO> {
        val sources = sourceService.getSources()
        return mapper.mapAsList(sources, SourceDTO::class.java)
    }

    fun getSource(id: String): SourceDTO? {
        val source = sourceService.getSource(id)
        return mapper.map(source, SourceDTO::class.java)
    }

    fun updateSource(id: String, sourceRequest: SourceDTO) {
        val isValidFeed = rssFeedService.isValidFeed(sourceRequest.url)
        if (!isValidFeed) {
            throw InvalidSourceFormatException(sourceRequest.url)
        }
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