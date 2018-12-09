package com.discover.server.facade

import com.discover.server.domain.Response
import com.discover.server.domain.SearchCriteria
import com.discover.server.domain.Source
import com.discover.server.domain.SourceDTO
import com.discover.server.domain.User
import com.discover.server.exception.InvalidSourceFormatException
import com.discover.server.service.RssFeedService
import com.discover.server.service.SearchService
import com.discover.server.service.SourceService
import ma.glasnost.orika.MapperFacade
import org.springframework.stereotype.Service

@Service
class SourceFacade(private val mapper: MapperFacade,
                   private val sourceService: SourceService,
                   private val searchService: SearchService<Source>,
                   private val rssFeedService: RssFeedService) {

    fun addSource(sourceRequest: SourceDTO, user: User): Response {
        val isValidFeed = rssFeedService.isValidFeed(sourceRequest.url)
        if (!isValidFeed) {
            throw InvalidSourceFormatException(sourceRequest.url)
        }
        val source = mapper.map(sourceRequest, Source::class.java)
        val addedSource = sourceService.addSource(source, user)
        return mapper.map(addedSource, Response::class.java)
    }

    fun getSources(): List<Response> {
        val sources = sourceService.getSources()
        return mapper.mapAsList(sources, Response::class.java)
    }

    fun getSource(id: String): Response {
        val source = sourceService.getSource(id)
        return mapper.map(source, Response::class.java)
    }

    fun updateSource(id: String, sourceRequest: SourceDTO) {
        val source = mapper.map(sourceRequest, Source::class.java)
        sourceService.updateSource(id, source)
    }

    fun deleteSource(id: String) {
        sourceService.deleteSourceById(id)
    }

    fun findAll(searchCriteria: SearchCriteria): List<Response> {
        val specifications = searchCriteria.criteria.map { searchService.getSearchPredicate(it) }.toSet()
        val matchingSources = sourceService.findAll(specifications)
        return mapper.mapAsList(matchingSources, Response::class.java)
    }
}