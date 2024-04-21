package ru.practicum.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.model.Comment;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Comment mapToComment(NewCommentDto newCommentDto) {
        return Comment.builder()
                .text(newCommentDto.getText())
                .build();
    }

    public CommentDto mapToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .stateComment(String.valueOf(comment.getStateComment()))
                .created(comment.getCreated().format(formatter))
                .updated(comment.getUpdated().format(formatter))
                .authorId(comment.getAuthor().getId())
                .eventId(comment.getEvent().getId())
                .build();
    }

    public List<CommentDto> mapToLisCommentDto(Iterable<Comment> comments) {
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentDtoList.add(mapToCommentDto(comment));
        }
        return commentDtoList;
    }
}
