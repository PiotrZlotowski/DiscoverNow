package com.discover.server.model

import java.time.LocalDateTime
import java.time.LocalDateTime.now
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "sources")
class Source(var name: String = "",
             var url: String = "",
             var refreshInterval: Int = 0,
             var timeCreated: LocalDateTime = now(),
             var lastRefresh: LocalDateTime = now(),
             @field:OneToMany(mappedBy = "source")
             var feeds: List<Feed> = emptyList()): AbstractJpaPersistable<Long>()