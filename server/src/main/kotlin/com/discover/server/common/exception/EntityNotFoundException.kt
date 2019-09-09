package com.discover.server.common.exception

import java.lang.RuntimeException

class EntityNotFoundException(val entityId: Long): RuntimeException("entity.not.found.exception")