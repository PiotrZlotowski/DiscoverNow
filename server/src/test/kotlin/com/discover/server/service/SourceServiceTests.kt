package com.discover.server.service

import com.discover.server.model.Source
import com.discover.server.model.User
import com.discover.server.repository.SourceRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDateTime

const val RSS_URL = "http://my-rss.com"
const val RSS_NAME = "My RSS"

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
class SourceServiceTests {

    @InjectMockKs
    private lateinit var sut: SourceService

    @MockK
    private lateinit var sourceRepository: SourceRepository


    @Test
    fun `updateLastRefresh should updated to the given refresh time whenever the time is provided`() {
        // GIVEN
        val givenSource = Source(name = RSS_NAME, url = RSS_URL)
        val givenRefreshTime = LocalDateTime.now()
        val givenSources = listOf(givenSource)
        // AND
        every { sourceRepository.save(any<Source>()) } returns givenSource

        // WHEN
        sut.updateLastRefresh(givenSources, givenRefreshTime)

        // THEN
        then(givenSources).isNotEmpty
        then(givenSources.first().lastRefresh).isEqualTo(givenRefreshTime)

        verify { sourceRepository.save(givenSource) }
    }


    @Test
    fun `addSource should add the users to the subscribers whenever it already exists`() {
        // GIVEN
        val givenSource = Source(name = RSS_NAME, url = RSS_URL)
        val givenUser = User(email = "abc@gmail.com", password = "pwd1", roles = emptySet(), sources = emptyList())
        val givenAlreadyDefinedSource = Source(name = "My RSS", url = RSS_URL)
        // AND
        every { sourceRepository.findSourceByUrl(givenSource.url) } returns givenAlreadyDefinedSource

        // WHEN
        val actual = sut.addSource(givenSource, givenUser)

        // THEN
        then(actual).isNotNull
        then(actual.users).hasSize(1).first().isEqualTo(givenUser)

        verify { sourceRepository.findSourceByUrl(givenSource.url) }
    }

    @Test
    fun `updateSource should update url, name and refreshInterval whenever provided`() {
        // GIVEN
        val givenSourceId = "123"
        val givenSourceData = Source(name = RSS_NAME, url = RSS_URL, refreshInterval = 20)
        val alreadyDefinedSource = Source()
        // AND
        every { sourceRepository.getOne(givenSourceId.toLong()) } returns alreadyDefinedSource

        // WHEN
        sut.updateSource(givenSourceId, givenSourceData)

        // THEN
        then(givenSourceData.url).isEqualTo(RSS_URL)
        then(givenSourceData.name).isEqualTo(RSS_NAME)
        then(givenSourceData.refreshInterval).isEqualTo(20)

        verify { sourceRepository.getOne(givenSourceId.toLong()) }
    }

    @Test
    fun `findAll should return Sources according to the given specification`() {
        // GIVEN
        val spec = mockk<Specification<Source>>()
        val givenSetOfSpecification = setOf(spec)
        val givenSource = Source()

        every { sourceRepository.findAll(any<Specification<Source>>()) } returns listOf(givenSource)

        // WHEN
        val actual = sut.findAll(givenSetOfSpecification)

        // THEN
        then(actual).isNotEmpty
                .first().isEqualTo(givenSource)

        verify { sourceRepository.findAll(any<Specification<Source>>()) }
    }


}