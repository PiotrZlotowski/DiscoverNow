package com.discover.server.domain

import javax.validation.Valid
import javax.validation.constraints.NotEmpty


sealed class Response {
    object EmptyResponse: Response()
    data class SuccessfulResponse<T>(val id: Long?, val wrappedObject: T): Response()
}


data class SetDTO<T>(
        @field:NotEmpty
        @field:Valid
        val values: Set<T>)