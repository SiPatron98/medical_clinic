package org.example.integration.support;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public interface WiremockTestSupport {

    Map<LocalDate, String> COVID_DATES = Map.of(
            LocalDate.of(2020, 3, 14), "covid-1.json",
            LocalDate.of(2021, 2, 9), "covid-2.json",
            LocalDate.of(2021, 3, 25), "covid-3.json"
    );

    default void stubForReport(WireMockServer wireMockServer) {

        COVID_DATES.forEach((date, fileName) ->
                wireMockServer.stubFor(get(urlPathEqualTo("/reports/total"))
                        .withQueryParams(Map.of("date", equalTo(date.toString())))
                        .willReturn(aResponse()
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBodyFile("wiremock/%s".formatted(fileName)))));
    }
}
