package ru.practicum.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping()
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/users/{userId}/events/{eventId}/comments")
    public ResponseEntity<CommentDto> add(@PathVariable Long userId,
                                          @PathVariable Long eventId,
                                          @RequestBody @Validated NewCommentDto newCommentDto) {
        log.info("Добавление нового комментария к событию {} пользователем {}", eventId, userId);
        return new ResponseEntity<>(commentService.add(userId, eventId, newCommentDto), HttpStatus.CREATED);
    }

    @GetMapping("/events/{eventId}/comments")
    public List<CommentDto> getEventComments(@PathVariable Long eventId,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                             @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Просмотр всех комментарий у события с id = {}", eventId);
        return commentService.getEventComments(eventId, from, size);
    }

    @GetMapping("/comments/{commentId}")
    public CommentDto getCommentById(@PathVariable Long commentId) {
        log.info("Просмотр комментария с id = {}", commentId);
        return commentService.getCommentById(commentId);
    }

    @PatchMapping("/users/{userId}/comments/{commentId}")
    public CommentDto updateComment(@PathVariable Long userId,
                                    @PathVariable Long commentId,
                                    @RequestBody @Validated NewCommentDto newCommentDto) {
        log.info("Редактирование комментария с id = {} пользователем с id = {}", commentId, userId);
        return commentService.update(userId, commentId, newCommentDto);
    }

    @DeleteMapping("/users/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long commentId) {
        log.info("Удаление пользователем с id = {} комментария с id = {}", userId, commentId);
        commentService.delete(userId, commentId);
    }

    @PatchMapping("/admin/comments/{commentId}")
    public CommentDto updateCommentModerationByAdmin(@PathVariable Long commentId, @RequestParam Boolean isModeration) {
        return commentService.updateCommentModerationByAdmin(commentId, isModeration);
    }
}

