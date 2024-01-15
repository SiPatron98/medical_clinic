package org.example.infrastructure.database.repository.mapper;

import org.example.domain.Address;
import org.example.domain.Doctor;
import org.example.infrastructure.database.entity.AddressEntity;
import org.example.infrastructure.database.entity.DoctorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressEntityMapper {

    @Mapping(target = "patient", ignore = true)
    Address map(AddressEntity addressEntity);
}
