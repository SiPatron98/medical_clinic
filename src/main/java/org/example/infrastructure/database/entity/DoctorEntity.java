package org.example.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "idNumber")
@ToString(of = {"idNumber", "name", "surname", "specialization"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctor")
public class DoctorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    Integer doctorId;

    @Column(name = "id_number")
    String idNumber;

    @Column(name = "name")
    String name;

    @Column(name = "surname")
    String surname;

    @Column(name = "specialization")
    String specialization;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "doctor")
    Set<CalendarEntity> calendar;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "doctor")
    Set<AppointmentEntity> appointments;
}
