package com.discover.server.service

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.rometools.rome.feed.synd.SyndContentImpl
import com.rometools.rome.feed.synd.SyndEntryImpl
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import java.io.InputStream
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneOffset
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension::class)
class RssFeedServiceTest {

    @InjectMocks
    private lateinit var rssFeedService: RssFeedService

    @Mock
    private lateinit var restTemplate: RestTemplate

    @Mock
    private lateinit var feedInput: SyndFeedInput


    @Test
    fun `getSourceItem should return empty collection when provided resource is empty`() {
        // given
        val resource = ""

        // when
        val actual = rssFeedService.getFeedBySite(resource)
        // then
        assertThat(actual)
                .isNotNull
                .isEmpty()

    }


    @Test
    fun `getSourceItem should return one item when provided resource is correct`() {
        // given
        val resource = "http://rss.com/rss"
        val syndEntry = SyndEntryImpl()

        val rssSourceItem = RssFeedItem(title = "Article 1",
                description = "Description1",
                link = "http://rss.com/article/1",
                timePublished = LocalDateTime.of(2018, Month.JANUARY, 1, 1, 0, 0),
                origin = resource)

        val syndContent = SyndContentImpl()
        syndContent.value = "Description1"
        syndEntry.link = "http://rss.com/article/1"
        syndEntry.title = "Article 1"
        syndEntry.description = syndContent
        syndEntry.publishedDate = Date.from(LocalDateTime.of(2018, Month.JANUARY, 1, 0, 0, 0).toInstant(ZoneOffset.UTC))

        val syndFeed: SyndFeed = mock {
            on { entries } doReturn listOf(syndEntry)
        }
        val responseInputStream: InputStream = mock()
        val responseResource: Resource = mock {
            on { inputStream } doReturn responseInputStream
        }
        val responseEntity: ResponseEntity<Resource> = mock {
            on{ body } doReturn responseResource
        }
        whenever(restTemplate.getForEntity(resource, Resource::class.java)) doReturn responseEntity
        whenever(feedInput.build(any<XmlReader>())) doReturn syndFeed

        // when
        val actual = rssFeedService.getFeedBySite(resource)
        // then
        assertThat(actual)
                .isNotNull
                .hasSize(1)
                .first().isEqualTo(rssSourceItem)
    }
}