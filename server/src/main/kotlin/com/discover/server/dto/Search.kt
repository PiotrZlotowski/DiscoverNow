package com.discover.server.dto


data class SearchCriteriaDTO(val searchCriteria: Set<SearchCriteria>)


data class SearchCriteria(val key: String, val value: String, val operator: Operator)


enum class Operator {
    EQUAL
}