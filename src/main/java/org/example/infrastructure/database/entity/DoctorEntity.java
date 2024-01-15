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
    private Integer doctorId;

    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "specialization")
    private String specialization;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "doctor")
    private Set<CalendarEntity> calendar;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "doctor")
    private Set<AppointmentEntity> appointments;
}
