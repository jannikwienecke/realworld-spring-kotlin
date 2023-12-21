package de.wienecke.realworld

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ArticleNotFoundException::class)
    fun handleArticleNotFoundException(ex: ArticleNotFoundException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(ex: UserNotFoundException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.UNAUTHORIZED)
    }


    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ValidationErrorResponse> {
        val errors = mapOf("title" to ex.message.toString())
        return ResponseEntity(ValidationErrorResponse(errors), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(PropertyValidationException::class)
    fun handlePropertyValidationException(ex: PropertyValidationException): ResponseEntity<ValidationErrorResponse> {
        val errors = mapOf(ex.propertyName to (ex.message ?: "Unknown error"))
        return ResponseEntity(ValidationErrorResponse(errors), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<ValidationErrorResponse> {
        val errors = ex.bindingResult.fieldErrors.associate { it.field to it.defaultMessage.orEmpty() }
        return ResponseEntity(ValidationErrorResponse(errors), HttpStatus.BAD_REQUEST)
    }


}

class ValidationErrorResponse(val errors: Map<String, String>)

class ArticleNotFoundException(slug: String) :
    RuntimeException("Article with slug '$slug' not found")

class UserNotFoundException(login: String) :
    RuntimeException("User with login '$login' not found")

class PropertyValidationException(
    val propertyName: String,
    errorMessage: String
) : RuntimeException("$propertyName: $errorMessage")