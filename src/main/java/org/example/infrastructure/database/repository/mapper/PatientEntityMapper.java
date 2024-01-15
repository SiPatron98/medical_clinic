package org.example.infrastructure.database.repository.mapper;

import org.example.domain.Address;
import org.example.domain.Patient;
import org.example.infrastructure.database.entity.PatientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PatientEntityMapper {

    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "diseases", ignore = true)
    @Mapping(target = "address", ignore = true)
    Patient mapFromEntity(PatientEntity entity);

    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "diseases", ignore = true)
    @Mapping(target = "address", ignore = true)
    PatientEntity mapToEntity(Patient patient);

}
