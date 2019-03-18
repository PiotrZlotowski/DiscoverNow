package com.discover.server.feed

import com.discover.server.common.AbstractJpaPersistable
import com.discover.server.authentication.User
import com.discover.server.source.Source
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "feeds")
class Feed(val title: String,
           val description: String,
           val url: String,
           val seen: Boolean = false,
           val timePublished: LocalDateTime = LocalDateTime.now(),
           @field:ManyToOne
           @field:JoinColumn(name = "source_id")
           val source: Source,
           @field:ManyToOne
           @field:JoinColumn(name = "user_id")
           val user: User) : AbstractJpaPersistable<Long>()


data class FeedDTO(val id: Long,
                   val title: String,
                   val description: String,
                   val url: String,
                   val timePublished: String)
