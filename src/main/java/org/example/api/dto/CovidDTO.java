package org.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CovidDTO {

    private LocalDate localDate;
    private Integer confirmedDiff;
    private Integer deathsDiff;
    private Integer recoveredDiff;
    private Integer activeDiff;
    private Integer confirmed;
    private Integer deaths;
    private Integer recovered;
    private Integer active;

}
