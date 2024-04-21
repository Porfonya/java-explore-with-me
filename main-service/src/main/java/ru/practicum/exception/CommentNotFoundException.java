package ru.practicum.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(long id) {

        super(String.format("Comment with id = %d was not found", id));
    }
}
