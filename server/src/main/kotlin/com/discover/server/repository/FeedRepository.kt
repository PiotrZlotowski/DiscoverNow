package com.discover.server.repository

import com.discover.server.model.Feed
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface FeedRepository: CrudRepository<Feed, Long>