package com.discover.server.domain

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

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


data class FeedDTO(val title: String,
                   val description: String,
                   val url: String,
                   val timePublished: String)
