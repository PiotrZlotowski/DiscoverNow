package com.discover.server.feed

import com.rometools.rome.feed.synd.SyndContentImpl
import com.rometools.rome.feed.synd.SyndEntryImpl
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import java.io.InputStream
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.util.*

@ExtendWith(MockKExtension::class)
class RssFeedServiceTests {

    @InjectMockKs
    private lateinit var rssFeedService: RssFeedService

    @MockK
    private lateinit var restTemplate: RestTemplate

    @MockK
    private lateinit var feedInput: SyndFeedInput


    @Test
    fun `getSourceItem should return empty collection when provided resource is empty`() {
        // GIVEN
        val resource = ""
        // WHEN
        val actual = rssFeedService.getFeedBySite(resource)
        // THEN
        assertThat(actual)
                .isNotNull
                .isEmpty()
    }


    @Test
    fun `getSourceItem should return one item when provided resource is correct`() {
        // GIVEN
        val syndFeed: SyndFeed = mockk()
        val responseInputStream: InputStream = mockk(relaxed = true)
        val responseResource: Resource = mockk()
        val responseEntity: ResponseEntity<Resource> = mockk()
        // AND
        val resource = "http://rss.com/rss"
        val syndEntry = SyndEntryImpl()

        val rssSourceItem = RssFeedItem(title = "Article 1",
                description = "Description1",
                url = "http://rss.com/article/1",
                timePublished = LocalDateTime.of(2018, Month.JANUARY, 1, 0, 0, 0),
                origin = resource)
        val syndContent = SyndContentImpl()
        syndContent.value = "Description1"
        syndEntry.link = "http://rss.com/article/1"
        syndEntry.title = "Article 1"
        syndEntry.description = syndContent
        val zdt =  LocalDateTime.of(2018, Month.JANUARY, 1, 0, 0, 0).atZone(ZoneId.systemDefault())
        syndEntry.publishedDate = Date.from(zdt.toInstant())
        // AND
        every { syndFeed.entries } returns listOf(syndEntry)
        every { responseResource.inputStream } returns responseInputStream
        every { responseEntity.body } returns responseResource
        every { restTemplate.getForEntity(resource, Resource::class.java) } returns responseEntity
        every { feedInput.build(any<XmlReader>()) } returns syndFeed
        // WHEN
        val actual = rssFeedService.getFeedBySite(resource)
        // THEN
        assertThat(actual)
                .isNotNull
                .hasSize(1)
                .first().isEqualTo(rssSourceItem)
    }
}