package org.example.domain;

import lombok.*;

import java.time.OffsetDateTime;

@With
@Value
@Builder
@EqualsAndHashCode(of = "calendarId")
@ToString(of = {"calendarId"})
public class Calendar {
    Integer calendarId;
    OffsetDateTime dateTime;
    Doctor doctor;
}
