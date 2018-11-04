package com.discover.server.dto


data class SearchCriteria(val criteria: Set<Criteria>)


data class Criteria(val key: String, val value: String, val operator: Operator)


enum class Operator {
    EQUAL
}