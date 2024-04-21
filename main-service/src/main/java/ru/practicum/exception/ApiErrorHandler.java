package ru.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApiErrorHandler {
    @ExceptionHandler()
    public ResponseEntity<Object> handleValidationExc(ValidationExc e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiErrorResponse response = new ApiErrorResponse(
                badRequest,
                e.getMessage(),
                e.getLocalizedMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, badRequest);
    }

    @ExceptionHandler()
    public ResponseEntity<Object> handleNotFoundExc(NotFoundException e) {
        HttpStatus badRequest = HttpStatus.NOT_FOUND;
        ApiErrorResponse response = new ApiErrorResponse(
                badRequest,
                "The required object was not found.",
                e.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, badRequest);
    }
    @ExceptionHandler()
    public ResponseEntity<Object> handleCommentNotFoundExc(CommentNotFoundException e) {
        HttpStatus badRequest = HttpStatus.NOT_FOUND;
        ApiErrorResponse response = new ApiErrorResponse(
                badRequest,
                "The required object was not found.",
                e.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, badRequest);
    }

    @ExceptionHandler()
    public ResponseEntity<Object> handleRequestNotFoundExc(RequestNotFoundException e) {
        HttpStatus badRequest = HttpStatus.NOT_FOUND;
        ApiErrorResponse response = new ApiErrorResponse(
                badRequest,
                "The required object was not found.",
                e.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, badRequest);
    }

    @ExceptionHandler()
    public ResponseEntity<Object> handleBadRequestExc(BadRequestException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiErrorResponse response = new ApiErrorResponse(
                badRequest,
                "Incorrectly made request.",
                e.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, badRequest);
    }

    @ExceptionHandler()
    public ResponseEntity<Object> handleForbiddenExc(ForbiddenException e) {
        HttpStatus badRequest = HttpStatus.FORBIDDEN;
        ApiErrorResponse response = new ApiErrorResponse(
                badRequest,
                "For the requested operation the conditions are not met.",
                e.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, badRequest);
    }

    @ExceptionHandler()
    public ResponseEntity<Object> handleConflictException(ConflictExc e) {
        HttpStatus badRequest = HttpStatus.CONFLICT;
        ApiErrorResponse response = new ApiErrorResponse(
                badRequest,
                "Integrity constraint has been violated.",
                e.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, badRequest);

    }
}