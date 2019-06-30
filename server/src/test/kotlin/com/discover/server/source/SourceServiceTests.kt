package com.discover.server.source

import com.discover.server.authentication.User
import com.discover.server.authentication.UserAlreadySubscribedException
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.catchThrowable
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDateTime

const val RSS_URL = "http://my-rss.com"
const val RSS_NAME = "My RSS"

@ExtendWith(MockKExtension::class)
class SourceServiceTests {

    @InjectMockKs
    private lateinit var sut: SourceService

    @MockK
    private lateinit var sourceRepository: SourceRepository


    @Test
    fun `updateLastRefresh should updated to the given refresh time whenever the time is provided`() {
        // GIVEN
        val source = Source(name = RSS_NAME, url = RSS_URL)
        val refreshTime = LocalDateTime.now()
        val sources = listOf(source)
        // AND
        every { sourceRepository.save(any<Source>()) } returns source

        // WHEN
        sut.updateLastRefresh(sources, refreshTime)

        // THEN
        then(sources).isNotEmpty
        then(sources.first().lastRefresh).isEqualTo(refreshTime)

        verify { sourceRepository.save(source) }
    }


    @Test
    fun `addSource should add the users to the subscribers whenever it already exists`() {
        // GIVEN
        val givenSource = Source(name = RSS_NAME, url = RSS_URL)
        val givenUser = User(email = "abc@gmail.com", password = "pwd1", roles = emptySet(), sources = emptyList(), compilations = emptySet())
        val givenAlreadyDefinedSource = Source(name = RSS_NAME, url = RSS_URL)
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
        val sourceId = "123"
        val sourceData = Source(name = RSS_NAME, url = RSS_URL, refreshInterval = 20)
        val alreadyDefinedSource = Source()
        // AND
        every { sourceRepository.getOne(sourceId.toLong()) } returns alreadyDefinedSource

        // WHEN
        sut.updateSource(sourceId, sourceData)

        // THEN
        then(sourceData.url).isEqualTo(RSS_URL)
        then(sourceData.name).isEqualTo(RSS_NAME)
        then(sourceData.refreshInterval).isEqualTo(20)

        verify { sourceRepository.getOne(sourceId.toLong()) }
    }

    @Test
    fun `findAll should return Sources according to the given specification`() {
        // GIVEN
        val spec = mockk<Specification<Source>>()
        val setOfSpecification = setOf(spec)
        val source = Source()

        every { sourceRepository.findAll(any<Specification<Source>>()) } returns listOf(source)

        // WHEN
        val actual = sut.findAll(setOfSpecification)

        // THEN
        then(actual).isNotEmpty
                .first().isEqualTo(source)

        verify { sourceRepository.findAll(any<Specification<Source>>()) }
    }

    @Test
    fun `addSource should throw UserAlreadySubscribedException whenever adding already subscribed source`() {
        // GIVEN
        val source = Source(name = RSS_NAME, url = RSS_URL, users = emptyList())
        val alreadySubscribedSource = Source(name = RSS_NAME, url = RSS_URL)
        val user = User(email = "abc@gmail.com", password = "pwd1", roles = emptySet(), sources = listOf(source), compilations = emptySet())
        source.users += user

        // AND
        every { sourceRepository.findSourceByUrl(source.url) } returns source

        // WHEN
        val actual = catchThrowable { sut.addSource(user = user, source = alreadySubscribedSource) }

        // THEN
        then(actual).isExactlyInstanceOf(UserAlreadySubscribedException::class.java)
    }


}