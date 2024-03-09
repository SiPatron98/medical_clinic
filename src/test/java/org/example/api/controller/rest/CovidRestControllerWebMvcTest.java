package org.example.api.controller.rest;

import lombok.AllArgsConstructor;
import org.example.api.dto.CovidDTO;
import org.example.api.dto.mapper.CovidDataMapper;
import org.example.business.CovidService;
import org.example.domain.CovidData;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CovidRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CovidRestControllerWebMvcTest {

    private MockMvc mockMvc;

    @MockBean
    private final CovidService covidService;
    @MockBean
    private final CovidDataMapper covidDataMapper;

    @Test
    void canGetCovidDataFromDate() throws Exception {
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
        CovidDTO covidDTO = CovidDTO.builder()
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

        // when, then
        Mockito.when(covidService.getDataFromSpecificDate(date))
                .thenReturn(covidData);

        Mockito.when(covidDataMapper.map(covidData))
                .thenReturn(covidDTO);

        mockMvc.perform(get(CovidRestController.API_COVID).param("localeDate", date.toString()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.confirmedDiff", Matchers.is(123)))
                .andReturn();
    }
}
