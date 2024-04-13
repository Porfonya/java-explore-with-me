package ru.practicum.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ConflictExc extends RuntimeException {
    public ConflictExc(String message) {
        super(message);
    }
}
