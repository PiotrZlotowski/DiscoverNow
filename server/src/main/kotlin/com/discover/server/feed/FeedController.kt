package com.discover.server.feed

import com.discover.server.common.SetDTO
import com.discover.server.authentication.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/feeds")
class FeedController(val feedFacade: FeedFacade) {

    @GetMapping("/")
    fun getCurrentUserFeeds(@AuthenticationPrincipal user: User) = feedFacade.getCurrentUserFeeds(user)

    @GetMapping("/seen")
    fun getCurrentUserSeenFeeds(@AuthenticationPrincipal user: User) = feedFacade.getCurrentUserSeenFeeds(user)

    @PostMapping("/markAsSeen")
    fun markFeedsAsSeen(@RequestBody @Valid feedIds: SetDTO<Long>, @AuthenticationPrincipal user: User) = feedFacade.markFeedsAsSeen(feedIds, user)

}