package com.discover.server.model

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "feeds")
class Feed(val title: String,
                val summary: String,
                val url: String,
                val seen: Boolean = false,
                val timeCreated: LocalDateTime = LocalDateTime.now(),
                @field:ManyToOne
                @field:JoinColumn(name = "source_id")
                val source: Source): AbstractJpaPersistable<Long>()