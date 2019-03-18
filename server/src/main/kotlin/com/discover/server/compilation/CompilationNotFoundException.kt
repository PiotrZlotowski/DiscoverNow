package com.discover.server.compilation

import java.lang.RuntimeException

class CompilationNotFoundException(val compilationId: Long): RuntimeException("compilation.not.found")