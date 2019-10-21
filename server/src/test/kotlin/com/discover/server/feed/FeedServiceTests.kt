package com.discover.server.feed

import com.discover.server.source.Source
import com.discover.server.authentication.User
import com.discover.server.authentication.CORRECT_USER_EMAIL
import com.discover.server.source.RSS_URL
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class FeedServiceTests {

    @InjectMockKs
    private lateinit var sut: FeedService

    @MockK
    private lateinit var feedRepository: FeedRepository

    @MockK
    private lateinit var newFeedExtractor: NewFeedExtractor


    @Test
    fun `addNewFeeds should not invoke the repository whenever there are no feeds to save`() {
        // GIVEN
        val user = User(email = CORRECT_USER_EMAIL, password = "pwd1", roles = emptySet(), sources = emptyList(), compilations = emptySet())
        val source = Source(name = "My RSS", url = RSS_URL, users = mutableListOf(user))
        val sources = listOf(source)
        val feedBySites = emptyMap<String, List<RssFeedItem>>()
        // AND
        every { newFeedExtractor.getFeedsToSave(sources = sources, feedBySites = feedBySites) } returns emptyList()
        // WHEN
        sut.addNewFeeds(feedBySites = feedBySites, sources = sources)

        // THEN
        verify(exactly = 0) { feedRepository.saveAll(emptyList()) }
    }

    @Test
    fun `addNewFeeds should invoke the repository whenever there are feeds to save`() {
        // GIVEN
        val user = User(email = CORRECT_USER_EMAIL, password = "pwd1", roles = emptySet(), sources = emptyList(), compilations = emptySet())
        val source = Source(name = "My RSS", url = RSS_URL, users = mutableListOf(user))
        val time = LocalDateTime.now()
        val rssFeedItem = RssFeedItem(title = "Feed Item#1", description = "Short Description", url = "$RSS_URL/items/1", timePublished = time, origin = RSS_URL)
        val sources = listOf(source)
        val feedBySites = mapOf(RSS_URL to listOf(rssFeedItem))
        val feed = Feed(title = "Feed Item#1", description = "Short Description", seen = false, source = source, timePublished = LocalDateTime.now(), url = "$RSS_URL/items/1", user = user, isDeleted = false)

        // AND
        every { newFeedExtractor.getFeedsToSave(sources = sources, feedBySites = feedBySites) } returns listOf(feed)
        every { feedRepository.saveAll(any<List<Feed>>()) } returns emptyList()
        // WHEN
        sut.addNewFeeds(feedBySites = feedBySites, sources = sources)

        // THEN
        verify { feedRepository.saveAll(any<List<Feed>>()) }
    }

}