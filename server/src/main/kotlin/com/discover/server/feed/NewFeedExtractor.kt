package com.discover.server.feed

import com.discover.server.source.Source
import org.jsoup.Jsoup
import org.springframework.stereotype.Service

@Service
class NewFeedExtractor(private val feedRepository: FeedRepository) {

    fun getFeedsToSave(sources: List<Source>, feedBySites: Map<String, List<RssFeedItem>>): List<Feed> {
        val notSeenFeedsByUser = feedRepository.findBySeen(seen = false).groupBy { it.user }
        val feedsToSave = sources.flatMap { source ->
            source.users.flatMap { user ->
                val userFeeds = notSeenFeedsByUser[user]
                feedBySites.filter { it.key == source.url }
                        .flatMap { it.value.toList() }
                        .asSequence()
                        .filter(isNewFeed(userFeeds))
                        .map {
                            Feed(url = it.url, title = it.title,
                                    user = user, description = parseHtmlDescription(it.description), timePublished = it.timePublished, seen = false,
                                    source = sources.first { source -> source.url == it.origin })
                        }
                        .toList()
            }
        }
        return feedsToSave
    }


    private fun parseHtmlDescription(description: String): String {
        return Jsoup.parse(description.substring(0, Math.min(MAX_LENGTH_DESCRIPTION, description.length))).text()
    }

    private fun isNewFeed(userFeeds: List<Feed>?): (RssFeedItem) -> Boolean {
        return {
            userFeeds?.any { feed -> feed.url == it.url }?.not() ?: true
        }
    }

}