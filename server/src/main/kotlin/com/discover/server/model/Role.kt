package com.discover.server.model

import java.io.Serializable
import javax.persistence.Column
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