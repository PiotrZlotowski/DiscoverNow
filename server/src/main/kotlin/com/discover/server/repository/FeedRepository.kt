package com.discover.server.repository

import com.discover.server.domain.Feed
import com.discover.server.domain.User
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface FeedRepository: CrudRepository<Feed, Long> {

    fun findByUserAndSeenOrderByTimeCreated(user: User, seen: Boolean = false): List<Feed>

    @Modifying
    @Query("Update Feed f set f.seen = true where f.id in ?1 and f.user = ?2")
    fun markFeedsAsSeen(feedIds: Set<String>, user: User)


    fun findBySeen(seen: Boolean): List<Feed>



}