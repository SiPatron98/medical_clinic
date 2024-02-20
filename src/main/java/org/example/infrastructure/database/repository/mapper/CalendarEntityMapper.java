package org.example.infrastructure.database.repository.mapper;

import org.example.domain.Calendar;
import org.example.domain.Doctor;
import org.example.infrastructure.database.entity.CalendarEntity;
import org.example.infrastructure.database.entity.DoctorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CalendarEntityMapper {

    @Mapping(target = "doctor", ignore = true)
    CalendarEntity map(Calendar calendar);

    default CalendarEntity mapToEntityWithDoctor(Calendar calendar) {
        CalendarEntity entity = map(calendar)
                .withDoctor(mapToDoctorEntity(calendar.getDoctor()));
        return entity;
    }

    default DoctorEntity mapToDoctorEntity(Doctor doctor) {
        return DoctorEntity.builder()
                .doctorId(doctor.getDoctorId())
                .idNumber(doctor.getIdNumber())
                .name(doctor.getName())
                .surname(doctor.getSurname())
                .specialization(doctor.getSpecialization())
                .build();
    }
}
