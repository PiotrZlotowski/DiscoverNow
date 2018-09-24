package com.discover.server.facade

import com.discover.server.service.FeedService
import com.discover.server.service.RssFeedService
import com.discover.server.service.SourceService
import org.springframework.stereotype.Service

@Service
class FeedFacade(private val rssFeedService: RssFeedService,
                 private val feedService: FeedService,
                 private val sourceService: SourceService) {

    fun updateFeedWithMostRecentItems() {
        val readyToProcessSources = sourceService.getReadyToProcessSourceUrls()
        val sourceUrls = sourceService.getOriginFromSources(readyToProcessSources)
        val feedBySites = rssFeedService.getFeedBySites(sourceUrls)
        feedService.addNewFeeds(feedBySites, readyToProcessSources)
        sourceService.updateLastRefreshToNow(readyToProcessSources)
    }
}