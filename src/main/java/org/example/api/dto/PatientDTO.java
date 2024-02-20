package org.example.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {

    @Size(min = 11, max = 11)
    @Pattern(regexp = "\\d{11}")
    String pesel;
    String name;
    String surname;
    @Size(min = 9, max = 15)
    @Pattern(regexp = "^[+]\\d{2}\\s\\d{3}\\s\\d{3}\\s\\d{3}$")
    String phone;
    @Email
    String email;

    public static PatientDTO buildDefaultPatient() {
        return PatientDTO.builder()
                .pesel("11122233344")
                .name("Adam")
                .surname("Pacjentowy")
                .phone("+48 123 456 789")
                .email("adam@pacjentowy.pl")
                .build();
    }

    public Map<String, String> asMap() {
        Map<String, String> result = new HashMap<>();
        ofNullable(pesel).ifPresent(value -> result.put("pesel", value));
        ofNullable(name).ifPresent(value -> result.put("name", value));
        ofNullable(surname).ifPresent(value -> result.put("surname", value));
        ofNullable(phone).ifPresent(value -> result.put("phone", value));
        ofNullable(email).ifPresent(value -> result.put("email", value));
        return result;
    }
}
