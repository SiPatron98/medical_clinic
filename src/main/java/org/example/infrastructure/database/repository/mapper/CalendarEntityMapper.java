package org.example.infrastructure.database.repository.mapper;

import org.example.domain.Address;
import org.example.domain.Calendar;
import org.example.infrastructure.database.entity.AddressEntity;
import org.example.infrastructure.database.entity.CalendarEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CalendarEntityMapper {

    @Mapping(target = "doctor", ignore = true)
    CalendarEntity map(Calendar entity);
}
