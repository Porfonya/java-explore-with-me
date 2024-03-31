package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.stats.model.EndpointHit;
import ru.practicum.stats.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ViewStatsRepository extends JpaRepository<EndpointHit, Integer> {


    @Query(value = "SELECT new ru.practicum.stats.model.ViewStats( s.app, s.uri, COUNT( DISTINCT s.ip)) " +
            "FROM EndpointHit s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(DISTINCT s.uri) DESC")
    List<ViewStats> findAllUrisWithUniqueIp(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.stats.model.ViewStats(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM EndpointHit s " +
            "WHERE s.uri IN :uris AND s.timestamp BETWEEN :start AND :end " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(DISTINCT s.uri) DESC")
    List<ViewStats> findUrisWithUniqueIp(@Param("uris") String[] uris,
                                         @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.stats.model.ViewStats(s.app, s.uri, COUNT(s.ip)) " +
            "FROM EndpointHit s WHERE s.timestamp BETWEEN :start AND :end " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.ip) DESC")
    List<ViewStats> findAllUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.stats.model.ViewStats( s.app, s.uri, COUNT(s.ip)) " +
            "FROM EndpointHit s " +
            "WHERE  s.uri IN :uris AND (s.timestamp BETWEEN :start AND :end) " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.ip) DESC")
    List<ViewStats> findUris(@Param("uris") String[] uris,
                             @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}
