package com.discover.server.compilation

import com.discover.server.common.AbstractJpaPersistable
import com.discover.server.entry.Entry
import com.discover.server.entry.EntryDTO
import com.discover.server.authentication.User
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "compilations")
class Compilation(var name: String,
                  @field:Enumerated(EnumType.STRING)
                  var type: CompilationType,
                  @field:ManyToOne
                  @field:JoinColumn(name = "user_id")
                  var user: User,
                  @field:OneToMany(mappedBy = "associatedCompilation", cascade = [CascadeType.REMOVE])
                  val entries: MutableSet<Entry>) : AbstractJpaPersistable<Long>()


data class CompilationDTO(val name: String,
                          val entries: Set<EntryDTO>? = emptySet())


data class CreateCompilationRequest(val name: String)