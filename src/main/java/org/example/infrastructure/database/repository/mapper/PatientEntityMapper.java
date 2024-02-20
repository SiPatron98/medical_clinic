package org.example.infrastructure.database.repository.mapper;

import org.example.domain.Address;
import org.example.domain.Disease;
import org.example.domain.Patient;
import org.example.infrastructure.database.entity.AddressEntity;
import org.example.infrastructure.database.entity.DiseaseEntity;
import org.example.infrastructure.database.entity.PatientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.HashSet;
import java.util.Set;

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

    default Patient mapFromEntityWithAddressAndDiseases(PatientEntity entity) {
        Patient patient = mapFromEntity(entity)
                .withAddress(addressEntityToAddress(entity.getAddress()))
                .withDiseases(mapDiseaseFromEntity(entity.getDiseases()));
        return patient;

    }

    default Address addressEntityToAddress(AddressEntity addressEntity) {
        return Address.builder()
                .addressId(addressEntity.getAddressId())
                .country(addressEntity.getCountry())
                .city(addressEntity.getCity())
                .postalCode(addressEntity.getPostalCode())
                .street(addressEntity.getStreet())
                .build();
    }

    default Set<Disease> mapDiseaseFromEntity(Set<DiseaseEntity> diseaseEntities) {
        Set<Disease> diseaseSet = new HashSet<>();
        diseaseEntities.forEach(
                diseaseEntity -> diseaseSet.add(Disease.builder()
                        .diseaseId(diseaseEntity.getDiseaseId())
                        .name(diseaseEntity.getName())
                        .build())
        );
        return diseaseSet;
    }

}
