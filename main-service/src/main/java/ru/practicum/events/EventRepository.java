package ru.practicum.events;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.events.model.Event;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Event findEventByIdAndInitiator(Long eventId, User user);

    Page<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    List<Event> findByInitiatorId(Long userId, Pageable pageable);



    @Query(value = "SELECT e " +
            "FROM Event e " +
            "WHERE 1 = 1  " +
            "AND (:users IS NULL OR e.initiator.id IN :users ) " +
            "AND(:states IS NULL OR  e.state IN :states) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (e.eventDate BETWEEN :rangeStart AND :rangeEnd)")
    List<Event> findEvents(@Param("users") Collection<Long> users,
                           @Param("states") Collection<String> states,
                           @Param("categories") Collection<Long> categories,
                           @Param("rangeStart") LocalDateTime rangeStart,
                           @Param("rangeEnd") LocalDateTime rangeEnd, Pageable pageable);

   /* @Query(value = "SELECT new ru.practicum.stats.model.ViewStats( s.app, s.uri, COUNT( DISTINCT s.ip)) " +
            "FROM EndpointHit s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(DISTINCT s.uri) DESC")
    List<ViewStats> findAllUrisWithUniqueIp(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);*/


}
