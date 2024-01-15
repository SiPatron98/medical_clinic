package org.example.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = "calendarId")
@ToString(of = {"calendarId"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "calendar")
public class CalendarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calendar_id")
    Integer calendarId;

    @Column(name = "date_time")
    OffsetDateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    DoctorEntity doctor;
}
