package com.discover.server.memo

import com.discover.server.common.AbstractJpaPersistable
import com.discover.server.entry.Entry
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "entry_memos")
class Memo(var content: String,
           var timeCreated: LocalDateTime,
           @field:ManyToOne
           @field:JoinColumn(name = "entry_id")
           var entry: Entry): AbstractJpaPersistable<Long>()

data class CreateMemoRequest(val content: String)

data class UpdateMemoRequest(val content: String)

data class MemoDTO(val content: String)

