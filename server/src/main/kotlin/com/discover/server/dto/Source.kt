package com.discover.server.dto

import javax.validation.constraints.NotEmpty

data class SourceRequest(@field:NotEmpty val url: String)