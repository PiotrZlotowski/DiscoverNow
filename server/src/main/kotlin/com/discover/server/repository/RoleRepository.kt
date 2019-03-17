package com.discover.server.repository

import com.discover.server.domain.user.Role
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository: CrudRepository<Role, Long>