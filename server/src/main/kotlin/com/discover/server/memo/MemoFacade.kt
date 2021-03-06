package com.discover.server.memo

import com.discover.server.common.annotation.Facade
import ma.glasnost.orika.MapperFacade

@Facade
class MemoFacade(private val memoService: MemoService,
                 private val mapper: MapperFacade) {


    fun updateMemo(memoId: Long, updateMemoRequest: UpdateMemoRequest): MemoDTO {
        val memo = mapper.map(updateMemoRequest, Memo::class.java)
        val updatedMemo = memoService.updateMemo(memoId, memo)
        return mapper.map(updatedMemo, MemoDTO::class.java)
    }

    fun deleteMemo(memoId: Long) {
        memoService.deleteMemo(memoId)
    }


}