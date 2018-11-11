package com.discover.server.facade

import com.discover.server.dto.Feed
import com.discover.server.model.User
import com.discover.server.service.FeedService
import com.discover.server.service.RssFeedService
import com.discover.server.service.SourceService
import ma.glasnost.orika.MapperFacade
import org.springframework.stereotype.Service

@Service
class FeedFacade(private val rssFeedService: RssFeedService,
                 private val feedService: FeedService,
                 private val sourceService: SourceService,
                 private val mapper: MapperFacade) {

    fun updateFeedWithMostRecentItems() {
        val readyToProcessSources = sourceService.getReadyToProcessSourceUrls()
        val sourceUrls = sourceService.getOriginFromSources(readyToProcessSources)
        val feedBySites = rssFeedService.getFeedBySites(sourceUrls)
        feedService.addNewFeeds(feedBySites, readyToProcessSources)
        sourceService.updateLastRefresh(readyToProcessSources)
    }


    fun getCurrentUserFeeds(user: User): List<Feed> {
        val currentUserFeeds = feedService.getCurrentUserFeeds(user)
        return mapper.mapAsList(currentUserFeeds, Feed::class.java)
    }

    fun getCurrentUserSeenFeeds(user: User): List<Feed> {
        val currentUserSeenFeeds = feedService.getCurrentUserSeenFeeds(user)
        return mapper.mapAsList(currentUserSeenFeeds, Feed::class.java)
    }

    fun markFeedsAsSeen(feedIds: Set<String>, user: User) = feedService.markFeedsAsSeen(feedIds, user)
}