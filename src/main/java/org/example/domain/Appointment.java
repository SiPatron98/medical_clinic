package org.example.domain;

import lombok.*;

import java.time.OffsetDateTime;

@With
@Value
@Builder
@EqualsAndHashCode(of = "idNumber")
@ToString(of = {"appointmentId", "idNumber", "note"})
public class Appointment {
    Integer appointmentId;
    String idNumber;
    OffsetDateTime dateTime;
    String note;
    Doctor doctor;
    Patient patient;
}
