package com.discover.server.configuration



import com.discover.server.compilation.Compilation
import com.discover.server.compilation.CompilationDTO
import com.discover.server.compilation.CreateCompilationRequest
import com.discover.server.entry.CreateEntryRequest
import com.discover.server.entry.Entry
import com.discover.server.feed.Feed
import com.discover.server.feed.FeedDTO
import com.discover.server.source.Source
import com.discover.server.source.SourceDTO
import com.discover.server.common.SetDtoRequestConverter
import ma.glasnost.orika.MapperFacade
import ma.glasnost.orika.converter.ConverterFactory
import org.springframework.context.annotation.Configuration
import ma.glasnost.orika.impl.DefaultMapperFactory
import org.springframework.context.annotation.Bean


@Configuration
class MapperConfiguration {

    @Bean
    fun mapperFactory(): MapperFacade {
        val mapperFactory = DefaultMapperFactory.Builder().mapNulls(false).build()
        val converterFactory = mapperFactory.converterFactory
        registerClassMap(mapperFactory)
        registerConverters(converterFactory)
        return mapperFactory.mapperFacade
    }

    private fun registerClassMap(mapperFactory: DefaultMapperFactory) {
        mapperFactory.classMap(SourceDTO::class.java, Source::class.java).byDefault()
        mapperFactory.classMap(FeedDTO::class.java, Feed::class.java).byDefault()
        mapperFactory.classMap(CreateCompilationRequest::class.java, Compilation::class.java).byDefault()
        mapperFactory.classMap(CompilationDTO::class.java, Compilation::class.java).byDefault()
        mapperFactory.classMap(CreateEntryRequest::class.java, Entry::class.java).byDefault()
    }

    private fun registerConverters(converterFactory: ConverterFactory) {
        converterFactory.registerConverter(SetDtoRequestConverter)
    }

}