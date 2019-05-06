package com.discover.server.feed

import com.discover.server.extension.logger
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime


@Component
class FeedScheduler(private val feedFacade: FeedFacade) {

    private val logger by logger()

    @Scheduled(fixedDelayString = "\${app.schedulers.feed.time}", initialDelayString = "\${app.schedulers.feed.delay.time}")
    fun schedule() {
        logger.info("Starting FeedScheduler task at ${LocalDateTime.now()}")
        feedFacade.updateFeedWithMostRecentItems()
        logger.info("Finished FeedScheduler task at ${LocalDateTime.now()}")
    }

}