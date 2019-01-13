package com.discover.server.exception

import java.lang.RuntimeException

class InvalidSourceFormatException(val url: String): RuntimeException("invalid.source.format")