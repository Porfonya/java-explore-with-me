package ru.practicum.requests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.enums.Status;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    Optional<List<Request>> findByRequesterId(Long userId);

    Request findRequestByIdAndRequesterId(Long requestId, Long userId);

    Request findByRequesterIdAndEventId(Long userId, Long eventId);

    List<Request> findAllByEventIdAndEventInitiatorId(Long eventId, Long userId);

    Integer countByEventIdAndStatus(Long eventId, Status status);

    List<Request> findRequestsByEventId(Long eventId);
}
