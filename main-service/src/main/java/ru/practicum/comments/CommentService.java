package ru.practicum.comments;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto add(Long userId, Long eventId, NewCommentDto newCommentDto);

    List<CommentDto> getEventComments(Long eventId, int from, int size);

    CommentDto getCommentById(Long commentId);

    CommentDto update(Long userId, Long commentId, NewCommentDto newCommentDto);

    void delete(Long userId, Long commentId);

    CommentDto updateCommentModerationByAdmin(Long commentId, Boolean isModeration);
}
