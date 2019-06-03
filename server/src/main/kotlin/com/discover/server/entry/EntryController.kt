package com.discover.server.entry

import com.discover.server.memo.CreateMemoRequest
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/entries")
class EntryController(private val entryFacade: EntryFacade) {

    @PostMapping("/{entryId}/memo")
    fun addMemo(@PathVariable entryId: Long, @RequestBody memoRequest: CreateMemoRequest) =  entryFacade.addMemo(entryId, memoRequest)

}