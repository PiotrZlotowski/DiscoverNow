package com.discover.server.memo

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/memos")
class MemoController(private val memoFacade: MemoFacade) {

    @PutMapping("/{memoId}")
    fun updateMemo(@PathVariable memoId: Long, @RequestBody updateMemoRequest: UpdateMemoRequest) = memoFacade.updateMemo(memoId, updateMemoRequest)

    @DeleteMapping("/{memoId}")
    fun deleteMemo(@PathVariable memoId: Long) = memoFacade.deleteMemo(memoId)

}