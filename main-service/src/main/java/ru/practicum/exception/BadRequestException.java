package ru.practicum.exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException() {
        super("Event must not be published");
    }
}
