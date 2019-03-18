package com.discover.server.source

import com.discover.server.common.AbstractJpaPersistable
import com.discover.server.feed.Feed
import com.discover.server.authentication.User
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.validation.constraints.NotEmpty

@Entity
@Table(name = "sources")
class Source(var name: String = "",
             var url: String = "",
             var refreshInterval: Int = 0,
             var timeCreated: LocalDateTime = now(),
             var lastRefresh: LocalDateTime = now(),
             @field:OneToMany(mappedBy = "source")
             var feeds: List<Feed> = emptyList(),
             @field:ManyToMany
             @field:JoinTable(name = "user_sources",
                     joinColumns = [JoinColumn(name =  "source_id", referencedColumnName = "id")],
                     inverseJoinColumns = [JoinColumn(name =  "user_id", referencedColumnName = "id")])
             var users: List<User> = emptyList()) : AbstractJpaPersistable<Long>()

data class SourceDTO(@field:NotEmpty val url: String)