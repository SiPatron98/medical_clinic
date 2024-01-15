package org.example.domain;

import lombok.*;

import java.util.Set;

@With
@Value
@Builder
@EqualsAndHashCode(of = "idNumber")
@ToString(of = {"idNumber", "name", "surname", "specialization"})
public class Doctor {
    Integer doctorId;
    String idNumber;
    String name;
    String surname;
    String specialization;
    Set<Calendar> calendar;
    Set<Appointment> appointments;
}
