package ru.practicum.events;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.events.model.Event;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByIdIn(List<Long> events);

    Integer countEventsByCategoryId(Long catId);

    Event findEventByIdAndInitiator(Long eventId, User user);

    Optional<Event> findByIdAndState(Long eventId, String state);

    Page<Event> findByInitiatorId(Long userId, Pageable page);

    @Query(value = "SELECT * FROM Events e WHERE (:userId is null or e.initiator IN (cast(cast(:userId AS TEXT) AS BIGINT))) "
            + "and (:states is null or e.state IN (cast(:states AS text))) "
            + "and (:categories is null or e.category_id IN (cast(cast(:categories AS TEXT) AS BIGINT))) "
            + "and (cast(:rangeStart AS timestamp) is null or e.event_date >= cast(:rangeStart AS timestamp))"
            + "and (cast(:rangeEnd AS timestamp) is null or e.event_date < cast(:rangeEnd AS timestamp))",
            nativeQuery = true)
    List<Event> findEvents(@Param("userId") List<Long> userId, List<String> states, List<Long> categories,
                           @Param("rangeStart") LocalDateTime rangeStart,
                           @Param("rangeEnd") LocalDateTime rangeEnd, Pageable pageable);

    @Query(value = "SELECT * FROM Events e WHERE (e.state = 'PUBLISHED') "
            + "and (:text is null or lower(e.annotation) LIKE lower(concat('%',cast(:text AS text),'%')) "
            + "or lower(e.description) LIKE lower(concat('%',cast(:text AS text),'%'))) "
            + "and (:categories is null or e.category_id IN (cast(cast(:categories AS TEXT) AS BIGINT))) "
            + "and (:paid is null or e.paid = cast(cast(:paid AS text) AS BOOLEAN)) "
            + "and (e.event_date >= :rangeStart) "
            + "and (cast(:rangeEnd AS timestamp) is null or e.event_date < cast(:rangeEnd AS timestamp))",
            nativeQuery = true)
    List<Event> findPublishedEvents(String text, List<Long> categories, Boolean paid,
                                    @Param("rangeStart") LocalDateTime rangeStart,
                                    @Param("rangeEnd") LocalDateTime rangeEnd, Pageable pageable);


}
