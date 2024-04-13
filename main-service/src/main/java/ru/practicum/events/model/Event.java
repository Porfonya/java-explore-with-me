package ru.practicum.events.model;

import lombok.*;
import ru.practicum.categories.model.Category;
import ru.practicum.locations.Location;
import ru.practicum.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Builder
@Entity
@Setter
@Getter
@Table(name = "EVENTS", schema = "PUBLIC")
public class Event {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(name = "ANNOTATION")
    private String annotation;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;
    @Column(name = "CONFIRMED_REQUESTS")
    private Long confirmedRequests;
    @Column(name = "CREATED_ON")
    private LocalDateTime createdOn;
    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "EVENT_DATE")
    private LocalDateTime eventDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "INITIATOR")
    private User initiator;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LOCATION")
    private Location location;

    @Column(name = "PAID")
    private Boolean paid;
    @Column(name = "PARTICIPANT_LIMIT")
    private int participantLimit;
    @Column(name = "PUBLISHED_ON")
    private LocalDateTime publishedOn;
    @Column(name = "REQUEST_MODERATION")
    private Boolean requestModeration;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "STATE")
    private String state;
    @Column(name = "VIEWS")
    private Long views;

    public Event(Long id, String annotation, Category category, Long confirmedRequests, LocalDateTime createdOn, String description, LocalDateTime eventDate, User initiator, Location location, Boolean paid, int participantLimit, LocalDateTime publishedOn, Boolean requestModeration, String title, String state, Long views) {
        this.id = id;
        this.annotation = annotation;
        this.category = category;
        this.confirmedRequests = confirmedRequests;
        this.createdOn = createdOn;
        this.description = description;
        this.eventDate = eventDate;
        this.initiator = initiator;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.publishedOn = publishedOn;
        this.requestModeration = requestModeration;
        this.title = title;
        this.state = state;
        this.views = views;
    }
}
