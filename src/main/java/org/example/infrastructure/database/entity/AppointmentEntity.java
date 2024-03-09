package org.example.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@With
@Getter
@Setter
@EqualsAndHashCode(of = "idNumber")
@ToString(of = {"appointmentId", "idNumber", "note"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appointment")
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    Integer appointmentId;

    @Column(name = "id_number")
    String idNumber;

    @Column(name = "date_time")
    OffsetDateTime dateTime;

    @Column(name = "note")
    String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    DoctorEntity doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    PatientEntity patient;
}
