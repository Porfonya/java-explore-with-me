package ru.practicum.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ValidationExc extends RuntimeException {

    public ValidationExc(String message) {
        super(message);
    }
}
