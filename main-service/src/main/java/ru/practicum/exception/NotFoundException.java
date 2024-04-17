package ru.practicum.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(long id) {

        super(String.format("Event with id=%d was not found", id));
    }
}
