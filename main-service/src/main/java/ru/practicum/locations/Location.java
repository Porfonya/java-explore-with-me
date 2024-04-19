package ru.practicum.locations;

import lombok.*;

import javax.persistence.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Setter
@Getter
@Table(name = "LOCATIONS", schema = "PUBLIC")
public class Location {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "LAT")
    private Float lat;
    @Column(name = "LON")
    private Float lon;
}
