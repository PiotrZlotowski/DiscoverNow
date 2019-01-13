package com.discover.server.repository

import com.discover.server.domain.Source
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
