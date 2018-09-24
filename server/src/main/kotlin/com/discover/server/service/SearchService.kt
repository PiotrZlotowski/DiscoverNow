package com.discover.server.service

import com.discover.server.dto.Operator.EQUAL
import com.discover.server.dto.SearchCriteria
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

@Service
class SearchService<T> {

    fun getSearchPredicate(searchCriteria: SearchCriteria): Specification<T> {

        val queryPredicate: (Root<T>, CriteriaQuery<*>, CriteriaBuilder) -> Predicate = {
            root, query, criteriaBuilder ->
            when(searchCriteria.operator) {
                EQUAL -> criteriaBuilder.equal(root.get<Any>(searchCriteria.key), searchCriteria.value)
            }
        }

        return Specification(queryPredicate)
    }

}