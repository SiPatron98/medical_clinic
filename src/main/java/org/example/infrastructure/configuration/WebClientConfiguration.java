package org.example.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.infrastructure.covid.ApiClient;
import org.example.infrastructure.covid.api.ReportApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfiguration {

    @Value("${api.covid.url}")
    private String covidUrl;

    @Bean
    public WebClient webClient(ObjectMapper objectMapper) {
        final var strategies = ExchangeStrategies
            .builder()
            .codecs(configurer -> {
                configurer
                    .defaultCodecs()
                    .jackson2JsonEncoder(
                        new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
                configurer
                    .defaultCodecs()
                    .jackson2JsonDecoder(
                        new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
            }).build();
        return WebClient.builder()
            .exchangeStrategies(strategies)
            .build();
    }

    @Bean
    public ApiClient apiClient(final WebClient webClient) {
        ApiClient apiClient = new ApiClient(webClient);
        apiClient.setBasePath(covidUrl);
        return apiClient;
    }

    @Bean
    public ReportApi reportApi(final ApiClient apiClient) {
        return new ReportApi(apiClient);
    }

}
