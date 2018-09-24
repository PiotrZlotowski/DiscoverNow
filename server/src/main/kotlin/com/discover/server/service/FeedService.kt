package com.discover.server.service

import com.discover.server.model.Feed
import com.discover.server.model.Source
import com.discover.server.repository.FeedRepository
import org.springframework.stereotype.Service

@Service
class FeedService(private val feedRepository: FeedRepository) {

    fun addNewFeeds(feedBySites: Map<String, List<RssFeedItem>>, sources: List<Source>) {

        val feedsToSave = feedBySites.values.flatMap { it.toList() }.map {
            Feed(url = it.link, title = it.title,
                    summary = it.description, timeCreated = it.timePublished, seen = false,
                    source = sources.first { source -> source.url == it.origin })
        }.toList()

        feedRepository.saveAll(feedsToSave)
    }
}