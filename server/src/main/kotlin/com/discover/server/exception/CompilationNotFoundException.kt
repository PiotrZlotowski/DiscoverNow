package com.discover.server.exception

import java.lang.RuntimeException

class CompilationNotFoundException(val compilationId: Long): RuntimeException("compilation.not.found")