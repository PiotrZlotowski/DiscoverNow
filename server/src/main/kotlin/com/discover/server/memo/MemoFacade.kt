package com.discover.server.memo

import ma.glasnost.orika.MapperFacade
import org.springframework.stereotype.Service

@Service
class MemoFacade(private val memoService: MemoService,
                 private val mapper: MapperFacade) {


    fun addMemo(entryId: Long, createMemoRequest: CreateMemoRequest): MemoDTO {
        val memo = mapper.map(createMemoRequest, Memo::class.java)
        val savedMemo = memoService.addMemoToEntry(entryId, memo)
        return mapper.map(savedMemo, MemoDTO::class.java)
    }

    fun updateMemo(memoId: Long, updateMemoRequest: UpdateMemoRequest): MemoDTO {
        val memo = mapper.map(updateMemoRequest, Memo::class.java)
        val updatedMemo = memoService.updateMemo(memoId, memo)
        return mapper.map(updatedMemo, MemoDTO::class.java)
    }

    fun deleteMemo(memoId: Long) {
        memoService.deleteMemo(memoId)
    }


}