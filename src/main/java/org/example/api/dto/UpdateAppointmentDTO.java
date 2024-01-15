package org.example.api.dto;

import lombok.*;
import org.example.domain.Doctor;
import org.example.domain.Patient;

import java.time.OffsetDateTime;
@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAppointmentDTO {

    String idNumber;
    OffsetDateTime dateTime;
    String note;
    Doctor doctor;
    Patient patient;
}
