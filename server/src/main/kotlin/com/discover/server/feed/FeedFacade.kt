package com.discover.server.feed

import com.discover.server.common.SetDTO
import com.discover.server.authentication.User
import com.discover.server.common.annotation.Facade
import com.discover.server.source.SourceService
import ma.glasnost.orika.MapperFacade

@Facade
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


    fun getCurrentUserFeeds(user: User): List<FeedDTO> {
        val currentUserFeeds = feedService.getCurrentUserFeeds(user)
        return mapper.mapAsList(currentUserFeeds, FeedDTO::class.java)
    }

    fun getCurrentUserSeenFeeds(user: User): List<FeedDTO> {
        val currentUserSeenFeeds = feedService.getCurrentUserSeenFeeds(user)
        return mapper.mapAsList(currentUserSeenFeeds, FeedDTO::class.java)
    }

    fun markFeedsAsSeen(feeds: SetDTO<Long>, user: User) {
        val feedIds = mapper.map(feeds, Set::class.java)
        return feedService.markFeedsAsSeen(feedIds, user)
    }

    fun deleteFeed(feedId: Long, user: User) {
        return feedService.deleteFeed(feedId, user)
    }
}