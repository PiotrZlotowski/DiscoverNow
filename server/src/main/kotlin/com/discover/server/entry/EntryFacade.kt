package com.discover.server.entry

import com.discover.server.authentication.User
import com.discover.server.memo.CreateMemoRequest
import com.discover.server.memo.Memo
import com.discover.server.memo.MemoDTO
import com.discover.server.memo.MemoService
import ma.glasnost.orika.MapperFacade
import org.springframework.stereotype.Service

@Service
class EntryFacade(private val mapper: MapperFacade,
                  private val memoService: MemoService) {

    fun addMemo(entryId: Long, user: User, createMemoRequest: CreateMemoRequest): MemoDTO {
        val memo = mapper.map(createMemoRequest, Memo::class.java)
        val savedMemo = memoService.addMemoToEntry(entryId, user, memo)
        return mapper.map(savedMemo, MemoDTO::class.java)
    }
}