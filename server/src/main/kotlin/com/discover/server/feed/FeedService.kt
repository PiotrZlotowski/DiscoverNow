package com.discover.server.feed

import com.discover.server.source.Source
import com.discover.server.authentication.User
import org.springframework.stereotype.Service

const val MAX_LENGTH_DESCRIPTION = 2499

@Service
class FeedService(private val feedRepository: FeedRepository, private val newFeedExtractor: NewFeedExtractor) {

    fun addNewFeeds(feedBySites: Map<String, List<RssFeedItem>>, sources: List<Source>) {
        val feedsToSave = newFeedExtractor.getFeedsToSave(sources, feedBySites)
        if (!feedsToSave.isNullOrEmpty()) {
            feedRepository.saveAll(feedsToSave)
        }
    }

    fun getCurrentUserFeeds(user: User) = feedRepository.findByUserAndSeenOrderByTimePublished(user)

    fun getCurrentUserSeenFeeds(user: User) = feedRepository.findByUserAndSeenOrderByTimePublished(user, true)

    fun markFeedsAsSeen(feedIds: Set<*>, user: User) = feedRepository.markFeedsAsSeen(feedIds, user)

    fun deleteAllFeeds(feeds: List<Feed>) = feedRepository.deleteAll(feeds)
}