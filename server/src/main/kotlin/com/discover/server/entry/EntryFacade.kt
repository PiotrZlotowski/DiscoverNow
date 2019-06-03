package com.discover.server.entry

import com.discover.server.memo.CreateMemoRequest
import com.discover.server.memo.Memo
import com.discover.server.memo.MemoDTO
import com.discover.server.memo.MemoService
import ma.glasnost.orika.MapperFacade
import org.springframework.stereotype.Service

@Service
class EntryFacade(private val mapper: MapperFacade,
                  private val memoService: MemoService) {

    fun addMemo(entryId: Long, createMemoRequest: CreateMemoRequest): MemoDTO {
        val memo = mapper.map(createMemoRequest, Memo::class.java)
        val savedMemo = memoService.addMemoToEntry(entryId, memo)
        return mapper.map(savedMemo, MemoDTO::class.java)
    }
}