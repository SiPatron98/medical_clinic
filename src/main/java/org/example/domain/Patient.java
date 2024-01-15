package org.example.domain;

import lombok.*;

import java.util.Set;

@With
@Value
@Builder
@EqualsAndHashCode(of = "pesel")
@ToString(of = {"patientId", "name", "surname", "email"})
public class Patient {
    Integer patientId;
    String name;
    String surname;
    String phone;
    String pesel;
    String email;
    Address address;
    Set<Disease> diseases;
    Set<Appointment> appointments;
}
