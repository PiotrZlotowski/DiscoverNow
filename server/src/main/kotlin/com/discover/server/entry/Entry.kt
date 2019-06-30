package com.discover.server.entry

import com.discover.server.compilation.Compilation
import com.discover.server.common.AbstractJpaPersistable
import com.discover.server.memo.Memo
import com.discover.server.memo.MemoDTO
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "compilation_entries")
class Entry(var title: String,
            var summary: String,
            var url: String,
            var keyTakeaway: String,
            var timeCreated: LocalDateTime?,
            @field: ManyToOne
            @field: JoinColumn(name = "compilation_id")
            var associatedCompilation: Compilation?,
            @field:OneToMany(mappedBy = "entry", cascade = [CascadeType.REMOVE])
            val memos: MutableSet<Memo>): AbstractJpaPersistable<Long>()


data class EntryDTO(var id: Long,
                    var title: String,
                    var summary: String,
                    var url: String,
                    var timeCreated: LocalDateTime,
                    var memos: Set<MemoDTO>?)


data class CreateEntryRequest(val title: String,
                        val summary: String,
                        val url: String)