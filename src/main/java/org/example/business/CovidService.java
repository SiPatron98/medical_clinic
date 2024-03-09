package org.example.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.business.dao.CovidDAO;
import org.example.domain.CovidData;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@AllArgsConstructor
public class CovidService {

    private final CovidDAO covidDAO;

    public CovidData getDataFromSpecificDate(LocalDate localDate) {

        return covidDAO.getDataByDate(localDate);
    }
}
