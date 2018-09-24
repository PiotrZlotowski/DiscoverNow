package com.discover.server.repository

import com.discover.server.model.Source
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SourceRepository: JpaRepository<Source, Long>, JpaSpecificationExecutor<Source> {

    @Query("SELECT s from Source s where datediff('second', s.lastRefresh, CURRENT_TIMESTAMP) > s.refreshInterval")
    fun findSourcesReadyToProcess(): List<Source>
}
