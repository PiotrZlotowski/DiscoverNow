package com.discover.server.feed

import com.discover.server.common.SetDTO
import com.discover.server.authentication.User
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteFeed(@PathVariable("id") feedId: Long, @AuthenticationPrincipal user: User) = feedFacade.deleteFeed(feedId, user)

}