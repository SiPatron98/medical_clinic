package org.example.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.example.business.dao.AppointmentDAO;
import org.example.domain.Address;
import org.example.domain.Appointment;
import org.example.domain.Calendar;
import org.example.domain.Disease;
import org.example.domain.exception.ProcessingException;
import org.example.infrastructure.database.entity.*;
import org.example.infrastructure.database.repository.jpa.AppointmentJpaRepository;
import org.example.infrastructure.database.repository.mapper.AppointmentEntityMapper;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class AppointmentRepository implements AppointmentDAO {

    private final AppointmentJpaRepository appointmentJpaRepository;
    private final AppointmentEntityMapper appointmentEntityMapper;

    static AtomicInteger idOfVisit = new AtomicInteger(4);
    @Override
    public List<Appointment> findAvailable() {
        return appointmentJpaRepository.findAll().stream()
                .map(appointmentEntityMapper::mapFromEntity)
                .toList();
    }

    @Override
    public Set<Appointment> findAvailableByDoctorsId(String numberId) {
        return appointmentJpaRepository.findAvailableByDoctorsId(numberId).stream()
                .map(appointmentEntityMapper::mapFromEntityWithDoctorAndPatient)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Appointment> findAvailableByPatientsPesel(String pesel) {
        return appointmentJpaRepository.findAvailableByPatientsPesel(pesel).stream()
                .map(appointmentEntityMapper::mapFromEntityWithDoctorAndPatient)
                .collect(Collectors.toSet());
    }

    @Override
    public Appointment findByIdNumber(String appointmentId) {
        return appointmentEntityMapper.mapFromEntityWithDoctorAndPatient(appointmentJpaRepository.findByIdNumber(appointmentId));
    }

    @Override
    public void save(Appointment appointment) {
        appointmentJpaRepository.saveAndFlush(AppointmentEntity.builder()
                        .appointmentId(appointment.getAppointmentId())
                        .idNumber(appointment.getIdNumber())
                        .dateTime(appointment.getDateTime())
                        .note(appointment.getNote())
                        .doctor(buildDoctorEnity(appointment))
                        .patient(buildPatientEntity(appointment))
                .build());
    }


    @Override
    public void makeAnAppointment(Appointment appointment) {

        appointmentJpaRepository.saveAndFlush(AppointmentEntity.builder()
                        .idNumber("00" + idOfVisit.addAndGet(1))
                        .dateTime(appointment.getDateTime())
                        .patient(buildPatientEntity(appointment))
                        .doctor(buildDoctorEnity(appointment))
                .build());

    }

    @Override
    public void update(Appointment appointment) {
        AppointmentEntity entity = appointmentJpaRepository.findByIdNumber(appointment.getIdNumber());
        entity.setNote(appointment.getNote());
        appointmentJpaRepository.saveAndFlush(entity);
    }

    private PatientEntity buildPatientEntity(Appointment appointment) {
        return PatientEntity.builder()
                .name(appointment.getPatient().getName())
                .surname(appointment.getPatient().getSurname())
                .pesel(appointment.getPatient().getPesel())
                .address(AddressEntity.builder()
                        .city(appointment.getPatient().getAddress().getCity())
                        .country(appointment.getPatient().getAddress().getCountry())
                        .postalCode(appointment.getPatient().getAddress().getPostalCode())
                        .street(appointment.getPatient().getAddress().getStreet())
                        .build())
                .email(appointment.getPatient().getEmail())
                .diseases(buildDiseases(appointment.getPatient().getDiseases()))
                .build();
    }

    private Set<DiseaseEntity> buildDiseases(Set<Disease> diseasesSet) {
        Set<DiseaseEntity> diseaseEntitySet = new HashSet<>();
        diseasesSet.forEach(
                disease -> diseaseEntitySet.add(DiseaseEntity.builder()
                        .name(disease.getName())
                        .build())
        );
        return diseaseEntitySet;
    }

    private DoctorEntity buildDoctorEnity(Appointment appointment) {
        return DoctorEntity.builder()
                .idNumber(appointment.getDoctor().getIdNumber())
                .name(appointment.getDoctor().getName())
                .surname(appointment.getDoctor().getSurname())
                .specialization(appointment.getDoctor().getSpecialization())
                .calendar(buildCalendarEntities(appointment.getDoctor().getCalendar()))
                .build();

    }

    private Set<CalendarEntity> buildCalendarEntities(Set<Calendar> calendarSet) {
        Set<CalendarEntity> calendarEntitySet = new HashSet<>();
        calendarSet.forEach(
                calendar -> calendarEntitySet.add(CalendarEntity.builder()
                        .dateTime(calendar.getDateTime())
                        .build())
        );
        return calendarEntitySet;
    }
}
