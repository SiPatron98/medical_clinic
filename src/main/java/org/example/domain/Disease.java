package org.example.domain;

import lombok.*;

@With
@Value
@Builder
@EqualsAndHashCode(of = "diseaseId")
@ToString(of = {"diseaseId", "name"})
public class Disease {

    Integer diseaseId;
    String name;
    Patient patient;
}
