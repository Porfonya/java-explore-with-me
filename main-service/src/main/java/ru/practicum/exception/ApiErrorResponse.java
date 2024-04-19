package ru.practicum.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ApiErrorResponse {

    private final HttpStatus httpStatus;
    private final String reason;
    private final String message;
    private final LocalDateTime timestamp;

}
