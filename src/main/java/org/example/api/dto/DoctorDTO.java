package org.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.Calendar;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Optional.ofNullable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {

    String idNumber;
    String name;
    String surname;
    String specialization;
    Set<Calendar> calendar;

    public static DoctorDTO buildDefaultDoctor() {
        return DoctorDTO.builder()
                .idNumber("4653")
                .name("Adam")
                .surname("Lekarski")
                .specialization("dermathologist")
                .calendar(Set.of())
                .build();
    }

    public Map<String, String> asMap() {
        Map<String, String> result = new HashMap<>();
        ofNullable(idNumber).ifPresent(value -> result.put("idNumber", value));
        ofNullable(name).ifPresent(value -> result.put("name", value));
        ofNullable(surname).ifPresent(value -> result.put("surname", value));
        ofNullable(specialization).ifPresent(value -> result.put("specialization", value));
        return result;
    }
}
