package com.discover.server.compilation

import java.lang.RuntimeException

class EntityNotFoundException(val entityId: Long): RuntimeException("entity.not.found.exception")