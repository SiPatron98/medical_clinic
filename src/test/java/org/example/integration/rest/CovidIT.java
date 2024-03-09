package org.example.integration.rest;

import org.example.api.dto.CovidDTO;
import org.example.integration.configuration.RestAssuredIntegrationTestBase;
import org.example.integration.support.CovidControllerTestSupport;
import org.example.integration.support.WiremockTestSupport;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class CovidIT  extends RestAssuredIntegrationTestBase
        implements CovidControllerTestSupport, WiremockTestSupport {


    @Test
    void thatFindingCovidDataWorksCorrectly() {
        // given
        LocalDate date1 = LocalDate.of(2020, 3, 14);
        LocalDate date2 = LocalDate.of(2021, 2, 9);
        LocalDate date3 = LocalDate.of(2021, 3, 25);
        stubForReport(wireMockServer);

        // when
        CovidDTO covidData1 = getCovidData(date1);
        CovidDTO covidData2 = getCovidData(date2);
        CovidDTO covidData3 = getCovidData(date3);

        // then
        assertThat(covidData1).hasFieldOrPropertyWithValue("confirmedDiff", 35);
        assertThat(covidData2).hasFieldOrPropertyWithValue("confirmedDiff", 3999);
        assertThat(covidData3).hasFieldOrPropertyWithValue("confirmedDiff", 34150);
    }


}
