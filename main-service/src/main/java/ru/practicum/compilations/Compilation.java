package ru.practicum.compilations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.model.Event;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Data
@Table(name = "COMPILATION", schema = "PUBLIC")
public class Compilation {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "PINNED")
    private Boolean pinned = false;
    @JoinColumn(name = "TITLE")
    private String title;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "COMPILATIONS_EVENTS",
            joinColumns =  @JoinColumn(name = "COMPILATION_ID") ,
            inverseJoinColumns =  @JoinColumn(name = "EVENT_ID") )
    private Set<Event> events = new HashSet<>();


}
