package com.discover.server.common.exception

import com.discover.server.authentication.UserAlreadySubscribedException
import com.discover.server.source.InvalidSourceFormatException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class BaseExceptionHandler : ResponseEntityExceptionHandler() {


    @ExceptionHandler(value = [UserAlreadySubscribedException::class, InvalidSourceFormatException::class, EntityNotFoundException::class])
    fun handleCustomExceptions(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        val httpHeaders = HttpHeaders()
        return when (ex) {
            is UserAlreadySubscribedException -> handleUserAlreadySubscribedException(ex, httpHeaders, request)
            is InvalidSourceFormatException -> handleInvalidSourceFormatException(ex, httpHeaders, request)
            is EntityNotFoundException -> handleEntityNotFoundException(ex, httpHeaders, request)
            else -> throw ex
        }
    }


    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val bindingResult = ex.bindingResult
        val fieldErrors = bindingResult.fieldErrors.map { FieldErrors(it.field, it.defaultMessage) }
        val errorResponse = ErrorResponse("Validation Failed", fieldErrors)
        return ResponseEntity(errorResponse, headers, status)
    }

    fun handleUserAlreadySubscribedException(ex: UserAlreadySubscribedException, headers: HttpHeaders, request: WebRequest): ResponseEntity<Any> {
        val status = HttpStatus.BAD_REQUEST
        val errorResponse = ErrorResponse("${ex.message} ${ex.url}", emptyList())
        return ResponseEntity(errorResponse, headers, status)
    }

    fun handleInvalidSourceFormatException(ex: InvalidSourceFormatException, headers: HttpHeaders, request: WebRequest): ResponseEntity<Any> {
        val status = HttpStatus.BAD_REQUEST
        val errorResponse = ErrorResponse("${ex.message} ${ex.url}", emptyList())
        return ResponseEntity(errorResponse, headers, status)
    }

    fun handleEntityNotFoundException(ex: EntityNotFoundException, headers: HttpHeaders, request: WebRequest): ResponseEntity<Any> {
        val status = HttpStatus.BAD_REQUEST
        val errorResponse = ErrorResponse("${ex.message} ${ex.entityId}", emptyList())
        return ResponseEntity(errorResponse, headers, status)
    }

}

data class ErrorResponse(val message: String, val fieldsErrors: List<FieldErrors>)
data class FieldErrors(val name: String, val message: String?)