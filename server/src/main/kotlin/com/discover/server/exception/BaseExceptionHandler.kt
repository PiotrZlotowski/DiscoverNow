package com.discover.server.exception

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class BaseExceptionHandler : ResponseEntityExceptionHandler() {


    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val bindingResult = ex.bindingResult
        val fieldErrors = bindingResult.fieldErrors.map { FieldErrors(it.field, it.defaultMessage) }
        val errorResponse = ErrorResponse("Validation Failed", fieldErrors)
        return ResponseEntity(errorResponse, headers, status);
    }

}

data class ErrorResponse(val message: String, val fieldsErrors: List<FieldErrors>)
data class FieldErrors(val name: String, val message: String?)