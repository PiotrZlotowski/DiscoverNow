package com.discover.server.configuration


import com.discover.server.domain.Feed
import com.discover.server.domain.FeedDTO
import com.discover.server.domain.Source
import com.discover.server.domain.SourceDTO
import com.discover.server.orika.converter.SetDtoRequestConverter
import ma.glasnost.orika.MapperFacade
import ma.glasnost.orika.converter.ConverterFactory
import org.springframework.context.annotation.Configuration
import ma.glasnost.orika.impl.DefaultMapperFactory
import org.springframework.context.annotation.Bean


@Configuration
class MapperConfiguration {

    @Bean
    fun mapperFactory(): MapperFacade {
        val mapperFactory = DefaultMapperFactory.Builder().build()
        val converterFactory = mapperFactory.converterFactory
        registerClassMap(mapperFactory)
        registerConverters(converterFactory)
        return mapperFactory.mapperFacade
    }

    private fun registerClassMap(mapperFactory: DefaultMapperFactory) {
        mapperFactory.classMap(SourceDTO::class.java, Source::class.java).byDefault()
        mapperFactory.classMap(FeedDTO::class.java, Feed::class.java).byDefault()
    }

    private fun registerConverters(converterFactory: ConverterFactory) {
        converterFactory.registerConverter(SetDtoRequestConverter)
    }

}