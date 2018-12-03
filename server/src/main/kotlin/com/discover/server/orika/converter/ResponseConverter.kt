package com.discover.server.orika.converter

import com.discover.server.domain.Feed
import com.discover.server.domain.FeedDTO
import com.discover.server.domain.Response
import com.discover.server.domain.Source
import com.discover.server.domain.SourceDTO
import ma.glasnost.orika.CustomConverter
import ma.glasnost.orika.MappingContext
import ma.glasnost.orika.metadata.Type
import java.util.*

class SourceResponseConverter: CustomConverter<Source, Response>() {

    override fun convert(source: Source, destinationType: Type<out Response>, mappingContext: MappingContext): Response {
        val sourceDTO = SourceDTO(source.url)
        return Response.SuccessfulResponse(id = source.id, wrappedObject = sourceDTO)
    }
}


class FeedResponseConverter: CustomConverter<Feed, Response>() {

    override fun convert(source: Feed, destinationType: Type<out Response>, mappingContext: MappingContext): Response {
        val feedDTO = FeedDTO(source.title, source.summary, source.url, source.timeCreated.toString())
        return Response.SuccessfulResponse(source.id, feedDTO)
    }
}

class OptionalConverter<T>: CustomConverter<Optional<T>, Response>() {

    override fun convert(source: Optional<T>, destinationType: Type<out Response>, mappingContext: MappingContext): Response {
        if (!source.isPresent) {
            return Response.EmptyResponse
        }
        val extractedSource: T = source.get()
        return mapperFacade.map(extractedSource, Response::class.java)

    }

}