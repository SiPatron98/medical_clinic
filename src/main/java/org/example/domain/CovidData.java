package org.example.domain;

import lombok.*;

import java.time.LocalDate;

@Value
@Builder
public class CovidData {
    LocalDate localDate;
    Integer confirmedDiff;
    Integer deathsDiff;
    Integer recoveredDiff;
    Integer activeDiff;
    Integer confirmed;
    Integer deaths;
    Integer recovered;
    Integer active;
}
