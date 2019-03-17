package com.discover.server.service

import com.discover.server.domain.Operator.EQUAL
import com.discover.server.domain.Criteria
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

@Service
class SearchService<T> {

    fun getSearchPredicate(criteria: Criteria): Specification<T> {

        val queryPredicate: (Root<T>, CriteriaQuery<*>, CriteriaBuilder) -> Predicate = {
            root, query, criteriaBuilder ->
            when(criteria.operator) {
                EQUAL -> criteriaBuilder.equal(root.get<Any>(criteria.key), criteria.value)
            }
        }

        return Specification(queryPredicate)
    }

}