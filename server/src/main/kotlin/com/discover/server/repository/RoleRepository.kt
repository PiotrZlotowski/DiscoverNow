package com.discover.server.repository

import com.discover.server.domain.Role
import org.springframework.data.repository.CrudRepository

interface RoleRepository: CrudRepository<Role, Long>