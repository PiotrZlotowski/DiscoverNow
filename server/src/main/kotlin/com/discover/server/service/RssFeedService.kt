package com.discover.server.service

import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class RssFeedService(private val restTemplate: RestTemplate, private val feedInput : SyndFeedInput) {

    fun getFeedBySite(resource: String): Set<RssFeedItem> {
        if (resource.isEmpty()) {
            return emptySet()
        }
        val responseEntity = restTemplate.getForEntity(resource, Resource::class.java)
        val responseInputStream = responseEntity.body?.inputStream
        val feed = feedInput.build(XmlReader(responseInputStream))

        return if (feed.entries.isNotEmpty()) {
            feed.entries.map { RssFeedItem(title = it.title, description = it.description.value,
                    link = it.link, timePublished = it.publishedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(), origin = resource)}.toSet()
        } else {
            emptySet()
        }

    }

    fun getFeedBySites(resources: Set<String>): Map<String, List<RssFeedItem>> = resources.flatMap { getFeedBySite(it) }.groupBy { it.origin }


}

data class RssFeedItem(val title: String, val description: String, val link: String, val timePublished: LocalDateTime, val origin: String)
