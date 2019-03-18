package com.discover.server.role

import com.discover.server.authentication.User
import com.discover.server.common.AbstractJpaPersistable
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "user_roles")
class Role(var role: String,
           @field:ManyToOne
           @field:JoinColumn(name = "user_id")
           var user: User): AbstractJpaPersistable<Long>()