package com.discover.server.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.validation.constraints.NotEmpty

data class SourceDTO(@field:NotEmpty val url: String)