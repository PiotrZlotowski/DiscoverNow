package com.discover.server.source

import java.lang.RuntimeException

class InvalidSourceFormatException(val url: String): RuntimeException("invalid.source.format")