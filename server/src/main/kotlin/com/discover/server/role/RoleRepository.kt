package com.discover.server.role

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository: CrudRepository<Role, Long>