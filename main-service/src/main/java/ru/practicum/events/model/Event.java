package ru.practicum.events.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.categories.model.Category;
import ru.practicum.compilations.Compilation;
import ru.practicum.locations.Location;
import ru.practicum.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Data
@Table(name = "EVENTS", schema = "PUBLIC")
public class Event {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "TITLE")
    private String title;

    @Column(name = "ANNOTATION")
    private String annotation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "EVENT_DATE")
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LOCATION")
    private Location location;
    @Column(name = "PAID")
    private Boolean paid;
    @Column(name = "PARTICIPANT_LIMIT")
    private int participantLimit;
    @Column(name = "REQUEST_MODERATION")
    private Boolean requestModeration;
    @Column(name = "CREATED_ON")
    private LocalDateTime createdOn;
    @Column(name = "PUBLISHED_ON")
    private LocalDateTime publishedOn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "INITIATOR")
    private User initiator;

    @Column(name = "STATE")
    private String state;

    @ManyToMany(mappedBy = "events")
    private List<Compilation> compilations = new ArrayList<>();

    @Column(name = "VIEWS")
    private Long views;

}
