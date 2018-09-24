package com.discover.server.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.validation.constraints.NotEmpty

data class SourceDTO(@field:JsonIgnore val id: String? = null,
                     @field:NotEmpty val name: String,
                     @field:NotEmpty val url: String,
                     val refreshInterval: Long)


data class SourceSearchDTO(val name: String)