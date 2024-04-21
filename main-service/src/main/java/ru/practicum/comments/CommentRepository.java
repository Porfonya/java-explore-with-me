package ru.practicum.comments;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.comments.model.Comment;
import ru.practicum.enums.State;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Integer countCommentsByEventIdAndAuthorId(Long eventId, Long userId);

    List<Comment> findAllByEventIdAndStateComment(Long eventId, String stateComment, Pageable page);

    Optional<Comment> findByIdAndAuthorId(Long commId, Long authorId);
}
