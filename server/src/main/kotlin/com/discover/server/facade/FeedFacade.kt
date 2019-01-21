package com.discover.server.facade

import com.discover.server.domain.Response
import com.discover.server.domain.SetDTO
import com.discover.server.domain.User
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


    fun getCurrentUserFeeds(user: User): List<Response> {
        val currentUserFeeds = feedService.getCurrentUserFeeds(user)
        return mapper.mapAsList(currentUserFeeds, Response::class.java)
    }

    fun getCurrentUserSeenFeeds(user: User): List<Response> {
        val currentUserSeenFeeds = feedService.getCurrentUserSeenFeeds(user)
        return mapper.mapAsList(currentUserSeenFeeds, Response::class.java)
    }

    fun markFeedsAsSeen(feeds: SetDTO<Long>, user: User) {
        val feedIds = mapper.map(feeds, Set::class.java)
        return feedService.markFeedsAsSeen(feedIds, user)
    }
}