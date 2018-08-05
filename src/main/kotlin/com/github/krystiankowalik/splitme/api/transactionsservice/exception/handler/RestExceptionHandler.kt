package com.github.krystiankowalik.splitme.api.transactionsservice.exception.handler

import com.github.krystiankowalik.splitme.api.transactionsservice.exception.*
import org.apache.http.conn.HttpHostConnectException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {

    override fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val error = "Malformed JSON request"
        return buildResponseEntity(ApiError(status = HttpStatus.BAD_REQUEST, message = error))
    }

    @ExceptionHandler(Exception::class)
    protected fun handleOtherExceptions(e: Exception): ResponseEntity<Any> {
        return buildStandardResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(ResourceAccessException::class)
    protected fun handleHttpHostConnectException(e: HttpHostConnectException): ResponseEntity<Any> {
        return buildResponseEntity(ApiError(status = HttpStatus.INTERNAL_SERVER_ERROR, message = "Could not connect to: ${e.host}"))
    }

    @ExceptionHandler(TransactionModificationException::class)
    protected fun handleInvitationAlreadExistsException(e: TransactionModificationException): ResponseEntity<Any> {
        return buildStandardResponseEntity(e, HttpStatus.NOT_ACCEPTABLE)
    }

    @ExceptionHandler(UserNotFoundException::class)
    protected fun handleUserNotFoundException(e: UserNotFoundException): ResponseEntity<Any> {
        return buildStandardResponseEntity(e, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(NotTransactionPartyException::class)
    protected fun handleUserNotFoundException(e: NotTransactionPartyException): ResponseEntity<Any> {
        return buildStandardResponseEntity(e, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(DueNotFoundException::class)
    protected fun handleDueNotFoundException(e: DueNotFoundException): ResponseEntity<Any> {
        return buildStandardResponseEntity(e, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(TransactionNotFoundException::class)
    protected fun handleTransactionNotFoundException(e: TransactionNotFoundException): ResponseEntity<Any> {
        return buildStandardResponseEntity(e, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(AuthenticationException::class)
    protected fun handleTokenNotActiveException(e: AuthenticationException): ResponseEntity<Any> {
        return buildStandardResponseEntity(e, HttpStatus.UNAUTHORIZED)

    }

    private fun buildStandardResponseEntity(e: Exception, httpStatus: HttpStatus): ResponseEntity<Any> {
        val apiError = ApiError(status = httpStatus, message = e.message.toString())
        return buildResponseEntity(apiError)
    }

    private fun buildResponseEntity(apiError: ApiError): ResponseEntity<Any> {
        return ResponseEntity(apiError, apiError.status)
    }


}