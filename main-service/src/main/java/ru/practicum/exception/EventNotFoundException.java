package ru.practicum.exception;

public class EventNotFoundException extends RuntimeException{
    public EventNotFoundException(long id) {

        super(String.format("Event with id=%d was not found", id));
    }
}
