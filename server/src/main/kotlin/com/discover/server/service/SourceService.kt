package com.discover.server.service

import com.discover.server.domain.Source
import com.discover.server.domain.User
import com.discover.server.exception.UserAlreadySubscribedException
import com.discover.server.repository.SourceRepository
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.transaction.Transactional

@Service
class SourceService(val sourceRepository: SourceRepository) {

    /**
     * Returns sources which are ready to process.
     * (Meaning that: datediff(current_timestamp, last_refresh_date) >= interval)
     * @return set of sources
     */
    fun getReadyToProcessSourceUrls(): List<Source> = sourceRepository.findSourcesReadyToProcess()

    fun getOriginFromSources(sources: List<Source>): Set<String> = sources.map { it.url }.toSet()

    fun updateLastRefresh(sources: List<Source>, newRefreshTime: LocalDateTime = LocalDateTime.now()) {
        sources.forEach {
            it.lastRefresh = newRefreshTime
            sourceRepository.save(it)
        }
    }

    // TODO: Maybe we should use ActiveMQ in order to inform everyone that the a new source was added.
    // TODO: Someone can process the source faster then the normal schedule
    fun addSource(source: Source, user: User): Source {
        /*
         * Whenever the source is not new, just add the user to the subscribers of the source
         */
        val foundSource = sourceRepository.findSourceByUrl(source.url)
        foundSource?.let {
            // TODO: Laziness issue?
            val isUserAlreadySubscribedToSource = it.users.contains(user)

            if (isUserAlreadySubscribedToSource) {
                throw UserAlreadySubscribedException(source.url)
            }

            it.users += user
            return it
        }
        addSourcePredefinedData(source)
        source.users += user
        return sourceRepository.save(source)
    }

    private fun addSourcePredefinedData(source: Source) {
        source.timeCreated = LocalDateTime.now()
        source.refreshInterval = 30
        source.name = source.url
    }

    fun getSources(): MutableList<Source> = sourceRepository.findAll()

    fun getSource(id: String) = sourceRepository.findById(id.toLong())

    @Transactional
    fun updateSource(id: String, source: Source) {
        val sourceToBeUpdated = sourceRepository.getOne(id.toLong())
        sourceToBeUpdated.name = source.name
        sourceToBeUpdated.url = source.url
        sourceToBeUpdated.refreshInterval = source.refreshInterval
    }

    fun deleteSourceById(id: String) = sourceRepository.deleteById(id.toLong())

    fun findAll(specifications: Set<Specification<Source>>): List<Source> {

        val finalSpecification = specifications.reduce { acc, specification
            -> acc.and(specification) }

        return sourceRepository.findAll { root, query, criteriaBuilder
            ->  finalSpecification.toPredicate(root, query, criteriaBuilder) }
    }

}