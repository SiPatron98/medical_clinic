package org.example.api.dto.mapper;

import org.example.api.dto.CovidDTO;
import org.example.domain.CovidData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CovidDataMapper {

    CovidDTO map(CovidData covidData);
}
