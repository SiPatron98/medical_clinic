package org.example.domain;

import lombok.*;

import java.time.LocalDate;

@With
@Value
@Builder
@EqualsAndHashCode(of = "localDate")
@ToString(of = {"localDate", "pcrTestsCount"})
public class CovidData {
    LocalDate localDate;
    Integer pcrTestsCount;
}
