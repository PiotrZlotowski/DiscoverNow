package com.discover.server.source

import com.discover.server.source.Source
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SourceRepository: JpaRepository<Source, Long>, JpaSpecificationExecutor<Source> {

    @Query("SELECT s from Source s JOIN FETCH s.users u " +
            "where datediff('second', s.lastRefresh, CURRENT_TIMESTAMP) > s.refreshInterval " +
            "or s.lastRefresh = null")
    fun findSourcesReadyToProcess(): List<Source>

    fun findSourceByUrl(url: String): Source?

}
