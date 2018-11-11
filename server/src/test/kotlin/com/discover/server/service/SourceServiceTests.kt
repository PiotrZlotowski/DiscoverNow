package com.discover.server.service

import com.discover.server.model.Source
import com.discover.server.model.User
import com.discover.server.repository.SourceRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

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
        val givenSource = Source(name = "My RSS", url = "http://my-rss.com")
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
        val givenSource = Source(name = "My RSS", url = "http://my-rss.com")
        val givenUser = User(email = "abc@gmail.com", password = "pwd1", roles = emptySet(), sources = emptyList())
        val givenAlreadyDefinedSource = Source(name = "My RSS", url = "http://my-rss.com")
        // AND
        every { sourceRepository.findSourceByUrl(givenSource.url) } returns givenAlreadyDefinedSource

        // WHEN
        val actual = sut.addSource(givenSource, givenUser)

        // THEN
        then(actual).isNotNull
        then(actual.users).hasSize(1).first().isEqualTo(givenUser)

        verify { sourceRepository.findSourceByUrl(givenSource.url) }
    }


}