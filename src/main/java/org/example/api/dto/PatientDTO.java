package org.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.Address;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {

    String pesel;
    String name;
    String surname;
    String phone;
    String email;
}
