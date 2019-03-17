package com.discover.server.service

import com.discover.server.domain.feed.Feed
import com.discover.server.domain.source.Source
import com.discover.server.domain.user.User
import com.discover.server.repository.FeedRepository
import org.jsoup.Jsoup
import org.springframework.stereotype.Service

const val MAX_LENGTH_DESCRIPTION = 2499

@Service
class FeedService(private val feedRepository: FeedRepository) {

    fun addNewFeeds(feedBySites: Map<String, List<RssFeedItem>>, sources: List<Source>) {
        val feedsToSave = getFeedsToSave(sources, feedBySites)
        if (!feedsToSave.isNullOrEmpty()) {
            feedRepository.saveAll(feedsToSave)
        }
    }

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

    fun parseHtmlDescription(description: String): String {
        return Jsoup.parse(description.substring(0, Math.min(MAX_LENGTH_DESCRIPTION, description.length))).text()
    }

    private fun isNewFeed(userFeeds: List<Feed>?): (RssFeedItem) -> Boolean {
        return {
            userFeeds?.any { feed -> feed.url == it.url }?.not() ?: true
        }
    }

    fun getCurrentUserFeeds(user: User) = feedRepository.findByUserAndSeenOrderByTimePublished(user)

    fun getCurrentUserSeenFeeds(user: User) = feedRepository.findByUserAndSeenOrderByTimePublished(user, true)

    fun markFeedsAsSeen(feedIds: Set<*>, user: User) = feedRepository.markFeedsAsSeen(feedIds, user)
}