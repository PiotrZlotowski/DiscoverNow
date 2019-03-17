package com.discover.server.domain

import javax.validation.Valid
import javax.validation.constraints.NotEmpty


data class SetDTO<T>(
        @field:NotEmpty
        @field:Valid
        val values: Set<T>)