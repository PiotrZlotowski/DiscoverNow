package com.discover.server.domain


sealed class Response {
    object EmptyResponse: Response()
    data class SuccessfulResponse<T>(val id: Long?, val wrappedObject: T): Response()
}