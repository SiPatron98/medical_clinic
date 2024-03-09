package org.example.business;

import org.example.business.dao.CovidDAO;
import org.example.domain.CovidData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class CovidServiceTest {

    @InjectMocks
    private CovidService covidService;

    @Mock
    private CovidDAO covidDAO;

    @Test
    void canGetDataFromSpecificDate() {
        // given
        LocalDate date = LocalDate.of(2022, 12, 24);
        CovidData covidData = CovidData.builder()
                .localDate(date)
                .confirmedDiff(123)
                .deathsDiff(31)
                .recoveredDiff(124)
                .activeDiff(456)
                .confirmed(1323234)
                .deaths(123421)
                .recovered(1233)
                .active(4214)
                .build();
        Mockito.when(covidDAO.getDataByDate(date))
                .thenReturn(covidData);

        // when
        CovidData result = covidService.getDataFromSpecificDate(date);

        // then
        Assertions.assertEquals(123, result.getConfirmedDiff());
        Assertions.assertEquals(31, result.getDeathsDiff());

    }
}
