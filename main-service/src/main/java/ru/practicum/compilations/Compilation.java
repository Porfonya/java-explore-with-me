package ru.practicum.compilations;

import lombok.*;
import ru.practicum.events.model.Event;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Data
@Setter
@Getter
@Table(name = "COMPILATIONS", schema = "PUBLIC")
public class Compilation {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "PINNED")
    private Boolean pinned;
    @Column(name = "TITLE", nullable = false)
    private String title;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "COMPILATIONS_EVENTS",
            joinColumns = {@JoinColumn(name = "COMPILATION_ID")},
            inverseJoinColumns = {@JoinColumn(name = "EVENT_ID")})
    private List<Event> events = new ArrayList<>();


}
