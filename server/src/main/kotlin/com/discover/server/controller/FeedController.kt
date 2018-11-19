package com.discover.server.controller

import com.discover.server.facade.FeedFacade
import com.discover.server.domain.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/feeds")
class FeedController(val feedFacade: FeedFacade) {

    @GetMapping("/")
    fun getCurrentUserFeeds(@AuthenticationPrincipal user: User) = feedFacade.getCurrentUserFeeds(user)

    @GetMapping("/seen")
    fun getCurrentUserSeenFeeds(@AuthenticationPrincipal user: User) = feedFacade.getCurrentUserSeenFeeds(user)

    @PostMapping("/markAsSeen")
    fun markFeedsAsSeen(feedIds: Set<String>, @AuthenticationPrincipal user: User) = feedFacade.markFeedsAsSeen(feedIds, user)


}