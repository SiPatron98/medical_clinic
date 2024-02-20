package org.example.business.dao;

import org.example.domain.CovidData;

import java.time.LocalDate;
import java.util.List;

public interface CovidDAO {

    List<CovidData> getAllData();

}
