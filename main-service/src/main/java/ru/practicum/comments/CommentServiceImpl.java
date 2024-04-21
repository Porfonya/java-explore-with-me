package ru.practicum.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.checker.Checker;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.enums.State;
import ru.practicum.events.EventRepository;
import ru.practicum.events.model.Event;
import ru.practicum.exception.CommentNotFoundException;
import ru.practicum.exception.ConflictExc;
import ru.practicum.exception.NotFoundException;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final Checker checker;
    private final CommentMapper commentMapper;

    @Override
    public CommentDto add(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User user = checker.checkerAndReturnUser(userId);
        Event event = eventRepository.findByIdAndState(eventId, String.valueOf(State.PUBLISHED))
                .orElseThrow(() -> new NotFoundException(eventId));
        int count = commentRepository.countCommentsByEventIdAndAuthorId(eventId, userId);
        if (count == 0) {
            Comment comment = commentMapper.mapToComment(newCommentDto);
            comment.setCreated(LocalDateTime.now());
            comment.setUpdated(LocalDateTime.now());
            comment.setEvent(event);
            comment.setAuthor(user);
            comment.setStateComment(String.valueOf(State.PENDING));

            Comment existComment = commentRepository.save(comment);
            return commentMapper.mapToCommentDto(existComment);
        } else {
            throw new ConflictExc(String.format("Пользователь с id = %d уже оставлял комментарий к событию с id = %d",
                    userId, eventId));
        }
    }

    @Override
    public CommentDto update(Long userId, Long commentId, NewCommentDto newCommentDto) {
        checker.checkerUser(userId);
        Comment comment = checker.checkerCommentReturn(commentId);
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictExc("Комментарий не может обновлять другой пользователь");
        }
        if (comment.getStateComment().equals(String.valueOf(State.PUBLISHED))) {
            throw new ConflictExc("Сообщение уже опубликовано");
        }

        comment.setText(newCommentDto.getText());
        comment.setUpdated(LocalDateTime.now());
        comment.setStateComment(String.valueOf(State.PENDING));

        return commentMapper.mapToCommentDto(comment);
    }

    @Override
    public List<CommentDto> getEventComments(Long eventId, int from, int size) {
        checker.checkerEvent(eventId);
        Pageable page = PageRequest.of(from / size, size);
        List<Comment> comments =
                commentRepository.findAllByEventIdAndStateComment(eventId, String.valueOf(State.PUBLISHED), page);
        if (!comments.isEmpty()) {
            return commentMapper.mapToLisCommentDto(comments);
        } else {
            throw new CommentNotFoundException(eventId);
        }
    }

    @Override
    public CommentDto getCommentById(Long commentId) {
        Comment comment = checker.checkerCommentReturn(commentId);
        return commentMapper.mapToCommentDto(comment);
    }


    @Override
    public void delete(Long userId, Long commentId) {
        Comment comment = commentRepository.findByIdAndAuthorId(commentId, userId).orElseThrow(
                () -> new CommentNotFoundException(commentId));
        commentRepository.delete(comment);
    }

    @Override
    public CommentDto updateCommentModerationByAdmin(Long commentId, Boolean isModeration) {
        Comment comment = checker.checkerCommentReturn(commentId);
        if (isModeration) {
            comment.setStateComment(String.valueOf(State.PUBLISHED));
        } else {
            comment.setStateComment(String.valueOf(State.CANCELED));
        }

        return commentMapper.mapToCommentDto(commentRepository.save(comment));
    }
}
