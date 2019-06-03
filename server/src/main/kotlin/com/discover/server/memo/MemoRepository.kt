package com.discover.server.memo

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MemoRepository: CrudRepository<Memo, Long>