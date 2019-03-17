package com.discover.server.configuration



import com.discover.server.domain.compilation.Compilation
import com.discover.server.domain.compilation.CompilationDTO
import com.discover.server.domain.compilation.CreateCompilationRequest
import com.discover.server.domain.compilation.CreateEntryRequest
import com.discover.server.domain.compilation.Entry
import com.discover.server.domain.feed.Feed
import com.discover.server.domain.feed.FeedDTO
import com.discover.server.domain.source.Source
import com.discover.server.domain.source.SourceDTO
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