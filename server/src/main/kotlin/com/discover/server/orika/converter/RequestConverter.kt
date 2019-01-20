package com.discover.server.orika.converter

import com.discover.server.domain.SetDTO
import ma.glasnost.orika.CustomConverter
import ma.glasnost.orika.MappingContext
import ma.glasnost.orika.metadata.Type

object SetDtoRequestConverter: CustomConverter<SetDTO<Any>, Set<Any>>() {

    override fun convert(source: SetDTO<Any>, destinationType: Type<out Set<Any>>, mappingContext: MappingContext): Set<Any> {
        return source.values
    }

}