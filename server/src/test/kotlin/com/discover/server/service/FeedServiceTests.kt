package com.discover.server.service

import com.discover.server.domain.feed.Feed
import com.discover.server.domain.source.Source
import com.discover.server.domain.user.User
import com.discover.server.repository.FeedRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.BDDAssertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
class FeedServiceTests {

    @InjectMockKs
    private lateinit var sut: FeedService

    @MockK
    private lateinit var feedRepository: FeedRepository


    @Test
    fun `should not invoke the repository whenever there are no feeds to save`() {
        // GIVEN
        val givenUser = User(email = CORRECT_USER_EMAIL, password = "pwd1", roles = emptySet(), sources = emptyList(), compilations = emptySet())
        val givenSource = Source(name = "My RSS", url = RSS_URL, users = listOf(givenUser))

        // AND
        every { feedRepository.findBySeen(seen = false) } returns emptyList()
        // WHEN
        sut.addNewFeeds(feedBySites = emptyMap(), sources = listOf(givenSource))

        // THEN
        verify(exactly = 0) { feedRepository.saveAll(emptyList()) }
    }

    @Test
    fun `should invoke the repository whenever there are feeds to save`() {
        // GIVEN
        val givenUser = User(email = CORRECT_USER_EMAIL, password = "pwd1", roles = emptySet(), sources = emptyList(), compilations = emptySet())
        val givenSource = Source(name = "My RSS", url = RSS_URL, users = listOf(givenUser))
        val givenTime = LocalDateTime.now()
        val rssFeedItem = RssFeedItem(title = "Feed Item#1", description = "Short Description", url = "$RSS_URL/items/1", timePublished = givenTime, origin = RSS_URL)

        val feedBySite = mapOf(RSS_URL to listOf(rssFeedItem))

        // AND
        every { feedRepository.findBySeen(seen = false) } returns emptyList()
        every { feedRepository.saveAll(any<List<Feed>>()) } returns emptyList()
        // WHEN
        sut.addNewFeeds(feedBySites = feedBySite, sources = listOf(givenSource))

        // THEN
        verify { feedRepository.saveAll(any<List<Feed>>()) }
    }

    @Test
    fun `should return exactly one feed whenever rss items are provided`() {
        // GIVEN
        val givenUser = User(email = CORRECT_USER_EMAIL, password = "pwd1", roles = emptySet(), sources = emptyList(), compilations = emptySet())
        val givenSource = Source(name = "My RSS", url = RSS_URL, users = listOf(givenUser))
        val givenTime = LocalDateTime.now()
        val rssFeedItem = RssFeedItem(title = "Feed Item#1", description = "Short Description", url = "$RSS_URL/items/1", timePublished = givenTime, origin = RSS_URL)

        val givenFeedBySite = mapOf(RSS_URL to listOf(rssFeedItem))

        // AND
        every { feedRepository.findBySeen(seen = false) } returns emptyList()
        every { feedRepository.saveAll(any<List<Feed>>()) } returns emptyList()
        // WHEN
        val actual = sut.getFeedsToSave(feedBySites = givenFeedBySite, sources = listOf(givenSource))

        // THEN
        then(actual).hasSize(1)
        then(!actual.first().seen)
        then(actual.first().title == "Feed Item#1")
        then(actual.first().url == "$RSS_URL/items/1")
        then(actual.first().timePublished == givenTime)
    }

    @Test
    fun `should return exactly zero feeds whenever rss items are already persisted and not seen yet`() {
        // GIVEN
        val givenUser = User(email = CORRECT_USER_EMAIL, password = "pwd1", roles = emptySet(), sources = emptyList(), compilations = emptySet())
        val givenSource = Source(name = "My RSS", url = RSS_URL, users = listOf(givenUser))
        val givenTime = LocalDateTime.now()
        val givenRssFeedItem = RssFeedItem(title = "Feed Item#1", description = "Short Description", url = "$RSS_URL/items/1", timePublished = givenTime, origin = RSS_URL)
        val givenAlreadySavedFeed = Feed(title = "", url = "$RSS_URL/items/1", source = givenSource, user = givenUser, timePublished = givenTime, seen = false, description = "")

        val givenFeedBySite = mapOf(RSS_URL to listOf(givenRssFeedItem))

        // AND
        every { feedRepository.findBySeen(seen = false) } returns listOf(givenAlreadySavedFeed)
        every { feedRepository.saveAll(any<List<Feed>>()) } returns emptyList()
        // WHEN
        val actual = sut.getFeedsToSave(feedBySites = givenFeedBySite, sources = listOf(givenSource))

        // THEN
        then(actual).hasSize(0)
    }


    @Test
    fun `should remove html tags whenever description has them`() {
        // GIVEN
        val givenUser = User(email = CORRECT_USER_EMAIL, password = "pwd1", roles = emptySet(), sources = emptyList(), compilations = emptySet())
        val givenSource = Source(name = "My RSS", url = RSS_URL, users = listOf(givenUser))
        val givenTime = LocalDateTime.now()
        val rssFeedItem = RssFeedItem(title = "Feed Item#1", description = "<html><p>Short Description</p></html>", url = "$RSS_URL/items/1", timePublished = givenTime, origin = RSS_URL)

        val givenFeedBySite = mapOf(RSS_URL to listOf(rssFeedItem))

        // AND
        every { feedRepository.findBySeen(seen = false) } returns emptyList()
        every { feedRepository.saveAll(any<List<Feed>>()) } returns emptyList()
        // WHEN
        val actual = sut.getFeedsToSave(feedBySites = givenFeedBySite, sources = listOf(givenSource))

        // THEN
        then(actual).isNotEmpty
        then(actual.first().description).isEqualTo("Short Description")
    }

}