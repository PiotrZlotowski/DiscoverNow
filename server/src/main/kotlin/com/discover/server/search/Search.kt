package com.discover.server.search

import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull


data class SearchCriteria(@field:Valid
                          @field:NotEmpty val criteria: Set<Criteria>)


data class Criteria(@field:NotBlank val key: String,
                    @field:NotBlank val value: String,
                    @field:NotNull val operator: Operator)


enum class Operator {
    EQUAL
}