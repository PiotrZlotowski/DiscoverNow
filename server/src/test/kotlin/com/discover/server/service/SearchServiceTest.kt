package com.discover.server.service

import com.discover.server.dto.Criteria
import com.discover.server.dto.Operator
import com.discover.server.model.Source
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class SearchServiceTests {

    var sut = SearchService<Source>()

    @Test
    fun `getSearchPredicate should build equal predicate when equal criteria is provided`() {
        // GIVEN
        val criteria = Criteria("key1", "value1", Operator.EQUAL)
        val criteriaBuilder = mockk<CriteriaBuilder>()
        val root = mockk<Root<Source>>()
        val criteriaQuery = mockk<CriteriaQuery<Source>>()
        val comparisonPredicate = mockk<Predicate>()
        // AND
        every { criteriaBuilder.equal(root.get<Any>(criteria.key), criteria.value) } returns  comparisonPredicate

        // WHEN
        val actual = sut.getSearchPredicate(criteria).toPredicate(root, criteriaQuery, criteriaBuilder)

        // THEN
        then(actual).isNotNull.isEqualTo(comparisonPredicate)
        verify { criteriaBuilder.equal(root.get<Any>(criteria.key), criteria.value) }

    }
}