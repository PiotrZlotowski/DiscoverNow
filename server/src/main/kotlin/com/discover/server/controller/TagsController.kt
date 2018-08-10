package com.discover.server.controller

import com.example.db.tables.Tags.TAGS
import org.jooq.DSLContext
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by dawid on 10.08.18.
 */

@RestController
@RequestMapping("/tag/")
class TagsController constructor(private val dslContext:DSLContext){


    @PostMapping("/amount")
    fun tagsAmount() : Int{
        return dslContext.select(TAGS.ID).from(TAGS).fetch().size
    }
}