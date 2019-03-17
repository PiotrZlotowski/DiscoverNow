package com.discover.server.service

import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.io.InputStream
import java.lang.Exception
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class RssFeedService(private val restTemplate: RestTemplate, private val feedInput : SyndFeedInput) {

    fun getFeedBySite(resource: String): Set<RssFeedItem> {
        if (resource.isEmpty()) {
            return emptySet()
        }
        val responseInputStream = downloadFeed(resource)
        val feed = feedInput.build(XmlReader(responseInputStream))

        return if (feed.entries.isNotEmpty()) {
            feed.entries.map { RssFeedItem(title = it.title, description = it.description.value,
                    url = it.link, timePublished = it.publishedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(), origin = resource)}.toSet()
        } else {
            emptySet()
        }

    }

    private fun downloadFeed(resource: String): InputStream? {
        val responseEntity = restTemplate.getForEntity(resource, Resource::class.java)
        val responseInputStream = responseEntity.body?.inputStream
        return responseInputStream
    }

    fun getFeedBySites(resources: Set<String>): Map<String, List<RssFeedItem>> = resources.flatMap { getFeedBySite(it) }.groupBy { it.origin }

    fun isValidFeed(resource: String): Boolean {
        val responseInputStream = downloadFeed(resource)
        return try {
            feedInput.build(XmlReader(responseInputStream))
            true
        } catch (ex: Exception) {
            false
        }
    }


}

data class RssFeedItem(val title: String, val description: String, val url: String, val timePublished: LocalDateTime, val origin: String)
