package com.discover.server.feed

import com.discover.server.authentication.User
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface FeedRepository: CrudRepository<Feed, Long> {

    fun findByUserAndSeenOrderByTimePublished(user: User, seen: Boolean = false): List<Feed>

    @Transactional
    @Modifying
    @Query("Update Feed f set f.seen = true where f.id in ?1 and f.user = ?2")
    fun markFeedsAsSeen(feedIds: Set<*>, user: User)


    fun findBySeen(seen: Boolean): List<Feed>



}