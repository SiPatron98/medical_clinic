package org.example.infrastructure.covid;

import org.example.domain.CovidData;
import org.example.infrastructure.covid.model.Total;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class CovidDataMapper {

    public CovidData map(LocalDate date, Total total) {
        var builder = CovidData.builder()
                .localDate(date);

        Optional.ofNullable(total)
                .ifPresent(data -> builder
                        .confirmedDiff(data.getConfirmedDiff())
                        .deathsDiff(data.getDeathsDiff())
                        .recoveredDiff(data.getRecoveredDiff())
                        .activeDiff(data.getActiveDiff())
                        .confirmed(data.getConfirmed())
                        .deaths(data.getDeaths())
                        .recovered(data.getRecovered())
                        .active(data.getActive())
                );

        return builder.build();
    }
}
