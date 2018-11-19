package com.discover.server.domain


data class SearchCriteria(val criteria: Set<Criteria>)


data class Criteria(val key: String, val value: String, val operator: Operator)


enum class Operator {
    EQUAL
}