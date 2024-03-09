package org.example.api.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.example.api.dto.CovidDTO;
import org.example.api.dto.mapper.CovidDataMapper;
import org.example.business.CovidService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@AllArgsConstructor
@RequestMapping(CovidRestController.API_COVID)
public class CovidRestController {

    public static final String API_COVID = "/api/covid";

    private final CovidService covidService;
    private final CovidDataMapper covidDataMapper;

    @Operation(summary = "Get covid data from specific date")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Covid data found",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = CovidDTO.class
                                    )
                            )
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid date supplied",
                    content = @Content)
    })
    @GetMapping
    public CovidDTO covidDataFromDate(
            @Parameter(description = "Date")
            @RequestParam(value = "localeDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate localDate
    ) {
        return covidDataMapper
                .map(covidService.getDataFromSpecificDate(localDate));
    }
}
