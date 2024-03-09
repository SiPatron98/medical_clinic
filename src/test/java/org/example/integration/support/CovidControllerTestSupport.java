package org.example.integration.support;

import io.restassured.specification.RequestSpecification;
import org.example.api.controller.rest.CovidRestController;
import org.example.api.dto.CovidDTO;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Map;

public interface CovidControllerTestSupport {

    RequestSpecification requestSpecification();

    default CovidDTO getCovidData(final LocalDate localeDate) {
        Map<String, Object> params = Map.of(
                "localeDate", localeDate.toString()
        );
        return requestSpecification()
                .params(params)
                .get(CovidRestController.API_COVID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .and()
                .extract()
                .as(CovidDTO.class);
    }
}
