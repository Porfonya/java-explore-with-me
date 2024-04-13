package ru.practicum.requests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.requests.dto.RequestDto;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequesterId(Long userId);
    Request findRequestByIdAndRequesterId(Long requestId, Long userId);
    Request findByRequesterIdAndEventId(Long userId, Long eventId);
}
