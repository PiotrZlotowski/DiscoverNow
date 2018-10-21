package com.discover.server.repository

import com.discover.server.model.Role
import org.springframework.data.repository.CrudRepository

interface RoleRepository: CrudRepository<Role, Long>