package org.example.infrastructure.database.repository.mapper;

import org.example.domain.*;
import org.example.infrastructure.database.entity.AppointmentEntity;
import org.example.infrastructure.database.entity.CalendarEntity;
import org.example.infrastructure.database.entity.DiseaseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AppointmentEntityMapper {

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    Appointment mapFromEntity(AppointmentEntity entity);

    default Appointment mapFromEntityWithDoctorAndPatient(AppointmentEntity entity) {
        return mapFromEntity(entity)
                .withPatient(Patient.builder()
                        .name(entity.getPatient().getName())
                        .surname(entity.getPatient().getSurname())
                        .pesel(entity.getPatient().getPesel())
                        .address(Address.builder()
                                .city(entity.getPatient().getAddress().getCity())
                                .country(entity.getPatient().getAddress().getCountry())
                                .postalCode(entity.getPatient().getAddress().getPostalCode())
                                .street(entity.getPatient().getAddress().getStreet())
                                .build())
                        .email(entity.getPatient().getEmail())
                        .diseases(mapDiseasesFromEntity(entity.getPatient().getDiseases()))
//                        .appointments(mapAppointmentsFromEntity(entity.getPatient().getAppointments()))
                        .build())
                .withDoctor(Doctor.builder()
                        .idNumber(entity.getDoctor().getIdNumber())
                        .name(entity.getDoctor().getName())
                        .surname(entity.getDoctor().getSurname())
                        .specialization(entity.getDoctor().getSpecialization())
                        .calendar(mapCalendarFromEntity(entity.getDoctor().getCalendar()))
                        .build());
    }

    default Set<Disease> mapDiseasesFromEntity(Set<DiseaseEntity> diseaseEntities) {
        Set<Disease> diseaseSet = new HashSet<>();
        diseaseEntities.forEach(
                diseaseEntity -> diseaseSet.add(Disease.builder()
                        .name(diseaseEntity.getName())
                        .build())
        );
        return diseaseSet;
    }

    default Set<Calendar> mapCalendarFromEntity(Set<CalendarEntity> calendarEntities) {
        Set<Calendar> calendarSet = new HashSet<>();
        calendarEntities.forEach(
                calendarEntity -> calendarSet.add(Calendar.builder()
                        .dateTime(calendarEntity.getDateTime())
                        .build())
        );
        return calendarSet;
    }

//    default Set<Appointment> mapAppointmentsFromEntity(Set<AppointmentEntity> appointmentEntities) {
//        Set<Appointment> appointmentSet = new HashSet<>();
//        appointmentEntities.forEach(
//                appointmentEntity -> appointmentSet.add(Appointment.builder()
//                        .name(appointmentEntity.getName())
//                        .build())
//        );
//        return appointmentSet;
//    }
}
