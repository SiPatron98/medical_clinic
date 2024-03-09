package org.example.infrastructure.covid;

import lombok.AllArgsConstructor;
import org.example.business.dao.CovidDAO;
import org.example.domain.CovidData;
import org.example.domain.exception.NotFoundException;
import org.example.infrastructure.covid.api.ReportApi;
import org.example.infrastructure.covid.model.Total;
import org.example.infrastructure.covid.model.TotalResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CovidClientImpl implements CovidDAO {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final String COUNTRY_ISO = "POL";

    private final ReportApi reportApi;
    private final CovidDataMapper covidDataMapper;


    @Override
    public CovidData getDataByDate(LocalDate localDate) {
        Optional<TotalResponse> totalResponse = callCovidData(localDate, COUNTRY_ISO);
        if (totalResponse.isPresent()) {
            Total data = totalResponse.get().getData();
            return covidDataMapper.map(localDate, data);
        } else {
            throw new NotFoundException("There is no data here!");
        }
    }

    private Optional<TotalResponse> callCovidData(LocalDate date, String countryCode) {
        return Optional.ofNullable(reportApi.total(
                date.format(DATE_TIME_FORMATTER),
                countryCode
        ).block());
    }


}
