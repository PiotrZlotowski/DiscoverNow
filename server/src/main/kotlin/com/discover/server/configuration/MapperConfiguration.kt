package com.discover.server.configuration


import com.discover.server.domain.Source
import com.discover.server.domain.SourceDTO
import ma.glasnost.orika.MapperFacade
import org.springframework.context.annotation.Configuration
import ma.glasnost.orika.impl.DefaultMapperFactory
import org.springframework.context.annotation.Bean


@Configuration
class MapperConfiguration {

    @Bean
    fun mapperFactory(): MapperFacade {
        val mapperFactory = DefaultMapperFactory.Builder().build()
        mapperFactory.classMap(SourceDTO::class.java, Source::class.java).byDefault()
        return mapperFactory.mapperFacade
    }

}