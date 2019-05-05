package com.discover.server.feed

import com.discover.server.authentication.CORRECT_USER_EMAIL
import com.discover.server.authentication.User
import com.discover.server.source.RSS_URL
import com.discover.server.source.Source
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.BDDAssertions.then

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
class NewFeedExtractorTests {

    @InjectMockKs
    private lateinit var sut: NewFeedExtractor

    @MockK
    private lateinit var feedRepository: FeedRepository
    @Test
    fun `getFeedsToSave should return exactly one feed whenever rss items are provided`() {
        // GIVEN
        val user = User(email = CORRECT_USER_EMAIL, password = "pwd1", roles = emptySet(), sources = emptyList(), compilations = emptySet())
        val source = Source(name = "My RSS", url = RSS_URL, users = listOf(user))
        val time = LocalDateTime.now()
        val rssFeedItem = RssFeedItem(title = "Feed Item#1", description = "Short Description", url = "$RSS_URL/items/1", timePublished = time, origin = RSS_URL)

        val givenFeedBySite = mapOf(RSS_URL to listOf(rssFeedItem))

        // AND
        every { feedRepository.findBySeen(seen = false) } returns emptyList()
        every { feedRepository.saveAll(any<List<Feed>>()) } returns emptyList()
        // WHEN
        val actual = sut.getFeedsToSave(feedBySites = givenFeedBySite, sources = listOf(source))

        // THEN
        then(actual).hasSize(1)
        then(!actual.first().seen)
        then(actual.first().title == "Feed Item#1")
        then(actual.first().url == "$RSS_URL/items/1")
        then(actual.first().timePublished == time)
    }

    @Test
    fun `getFeedsToSave should return exactly zero feeds whenever rss items are already persisted and not seen yet`() {
        // GIVEN
        val user = User(email = CORRECT_USER_EMAIL, password = "pwd1", roles = emptySet(), sources = emptyList(), compilations = emptySet())
        val source = Source(name = "My RSS", url = RSS_URL, users = listOf(user))
        val time = LocalDateTime.now()
        val rssFeedItem = RssFeedItem(title = "Feed Item#1", description = "Short Description", url = "$RSS_URL/items/1", timePublished = time, origin = RSS_URL)
        val alreadySavedFeed = Feed(title = "", url = "$RSS_URL/items/1", source = source, user = user, timePublished = time, seen = false, description = "")

        val givenFeedBySite = mapOf(RSS_URL to listOf(rssFeedItem))

        // AND
        every { feedRepository.findBySeen(seen = false) } returns listOf(alreadySavedFeed)
        every { feedRepository.saveAll(any<List<Feed>>()) } returns emptyList()
        // WHEN
        val actual = sut.getFeedsToSave(feedBySites = givenFeedBySite, sources = listOf(source))

        // THEN
        then(actual).hasSize(0)
    }


    @Test
    fun `getFeedsToSave should remove html tags whenever description has them`() {
        // GIVEN
        val user = User(email = CORRECT_USER_EMAIL, password = "pwd1", roles = emptySet(), sources = emptyList(), compilations = emptySet())
        val source = Source(name = "My RSS", url = RSS_URL, users = listOf(user))
        val time = LocalDateTime.now()
        val rssFeedItem = RssFeedItem(title = "Feed Item#1", description = "<html><p>Short Description</p></html>", url = "$RSS_URL/items/1", timePublished = time, origin = RSS_URL)

        val givenFeedBySite = mapOf(RSS_URL to listOf(rssFeedItem))

        // AND
        every { feedRepository.findBySeen(seen = false) } returns emptyList()
        every { feedRepository.saveAll(any<List<Feed>>()) } returns emptyList()
        // WHEN
        val actual = sut.getFeedsToSave(feedBySites = givenFeedBySite, sources = listOf(source))

        // THEN
        then(actual).isNotEmpty
        then(actual.first().description).isEqualTo("Short Description")
    }

}
