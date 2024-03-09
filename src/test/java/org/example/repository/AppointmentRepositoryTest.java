package org.example.repository;

import org.example.domain.*;
import org.example.infrastructure.database.entity.*;
import org.example.infrastructure.database.repository.AppointmentRepository;
import org.example.infrastructure.database.repository.jpa.AppointmentJpaRepository;
import org.example.infrastructure.database.repository.mapper.AppointmentEntityMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class AppointmentRepositoryTest {

    @InjectMocks
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentJpaRepository appointmentJpaRepository;
    @Mock
    private AppointmentEntityMapper appointmentEntityMapper;

    @Test
    void canFoundAvailable() {
        // given
        List<Appointment> appointments = buildAppointmentsList();
        List<AppointmentEntity> appointmentEntities = buildAppointmentEntitiesList();
        Mockito.when(appointmentJpaRepository.findAll())
                .thenReturn(appointmentEntities);

        for (int i = 0; i < appointmentEntities.size(); i++) {
            Mockito.when(appointmentEntityMapper.mapFromEntity(appointmentEntities.get(i)))
                    .thenReturn(appointments.get(i));
        }

        // when
        List<Appointment> result = appointmentRepository.findAvailable();

        // then
        assertThat(result).hasSize(2);

    }

    @Test
    void canFoundAvailableByDoctorsId() {
        // given
        Appointment appointment = buildAppointmentsList().get(0);
        PatientEntity patientEntity = buildPatientEntity();
        DoctorEntity doctorEntity = buildDoctorEntity();
        Patient patient = buildPatient();
        Doctor doctor = buildDoctor();
        AppointmentEntity appointmentEntity = buildAppointmentEntitiesList().get(0).withDoctor(doctorEntity).withPatient(patientEntity);
        String idDoctor = "4653";
        Mockito.when(appointmentJpaRepository.findAvailableByDoctorsId(idDoctor))
                .thenReturn(Set.of(appointmentEntity));
        Mockito.when(appointmentEntityMapper.mapFromEntityWithDoctorAndPatient(appointmentEntity))
                .thenReturn(appointment.withDoctor(doctor).withPatient(patient));


        // when
        Set<Appointment> result = appointmentRepository.findAvailableByDoctorsId(idDoctor);

        // then
        assertThat(result).hasSize(1);
        Assertions.assertTrue(result.contains(appointment));

    }

    @Test
    void canFoundAvailableByPatientsPesel() {
        // given
        Appointment appointment = buildAppointmentsList().get(1);
        PatientEntity patientEntity = buildPatientEntity();
        DoctorEntity doctorEntity = buildDoctorEntity();
        Patient patient = buildPatient();
        Doctor doctor = buildDoctor();
        AppointmentEntity appointmentEntity = buildAppointmentEntitiesList().get(1).withDoctor(doctorEntity).withPatient(patientEntity);
        String pesel = "11122233344";
        Mockito.when(appointmentJpaRepository.findAvailableByPatientsPesel(pesel))
                .thenReturn(Set.of(appointmentEntity));
        Mockito.when(appointmentEntityMapper.mapFromEntityWithDoctorAndPatient(appointmentEntity))
                .thenReturn(appointment.withDoctor(doctor).withPatient(patient));


        // when
        Set<Appointment> result = appointmentRepository.findAvailableByPatientsPesel(pesel);

        // then
        assertThat(result).hasSize(1);
        Assertions.assertTrue(result.contains(appointment));

    }

    @Test
    void canFoundByItsId() {
        // given
        Appointment appointment = buildAppointmentsList().get(0);
        String idNumber = "1134e1";
        PatientEntity patientEntity = buildPatientEntity();
        DoctorEntity doctorEntity = buildDoctorEntity();
        Patient patient = buildPatient();
        Doctor doctor = buildDoctor();
        AppointmentEntity appointmentEntity = buildAppointmentEntitiesList().get(0).withDoctor(doctorEntity).withPatient(patientEntity);
        Mockito.when(appointmentJpaRepository.findByIdNumber(idNumber))
                .thenReturn(appointmentEntity);
        Mockito.when(appointmentEntityMapper.mapFromEntityWithDoctorAndPatient(appointmentEntity))
                .thenReturn(appointment.withDoctor(doctor).withPatient(patient));


        // when
        Appointment result = appointmentRepository.findByIdNumber(idNumber);

        // then
        Assertions.assertEquals("Adam", result.getDoctor().getName());
        Assertions.assertEquals(
                OffsetDateTime.of(2023, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC),
                result.getDateTime()
        );
    }

    @Test
    void canSaveAppointment() {
        // given
        Address address = Address.builder()
                .addressId(1)
                .country("Poland")
                .city("Warsaw")
                .street("Malinowa")
                .postalCode("67-345")
                .patient(null)
                .build();
        Appointment appointment = buildAppointmentsList().get(1).withDoctor(buildDoctor()).withPatient(buildPatient().withAddress(address).withDiseases(Set.of()));
        AppointmentEntity appointmentEntity = buildAppointmentEntitiesList().get(1);
        Mockito.when(appointmentJpaRepository.saveAndFlush(appointmentEntity))
                .thenReturn(appointmentEntity);

        // when
        appointmentRepository.save(appointment);

        // then
        Mockito.verify(appointmentJpaRepository, Mockito.times(1)).saveAndFlush(Mockito.any());
    }

    @Test
    void canMakeAnAppointment() {
        // given
        Address address = Address.builder()
                .addressId(1)
                .country("Poland")
                .city("Warsaw")
                .street("Malinowa")
                .postalCode("67-345")
                .patient(null)
                .build();
        Appointment appointment = Appointment.builder()
                .dateTime(OffsetDateTime.of(2025, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .doctor(buildDoctor())
                .patient(buildPatient().withAddress(address).withDiseases(Set.of()))
                .build();
        AppointmentEntity appointmentEntity = AppointmentEntity.builder()
                .dateTime(OffsetDateTime.of(2025, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .build();
        Mockito.lenient().when(appointmentJpaRepository.saveAndFlush(appointmentEntity))
                .thenReturn(appointmentEntity);

        // when
        appointmentRepository.makeAnAppointment(appointment);

        // then
        Mockito.verify(appointmentJpaRepository, Mockito.times(1)).saveAndFlush(Mockito.any());
    }

    @Test
    void canAppointmentBeUpdated() {
        // given
        String newNote = "New note";
        Address address = Address.builder()
                .addressId(1)
                .country("Poland")
                .city("Warsaw")
                .street("Malinowa")
                .postalCode("67-345")
                .patient(null)
                .build();
        Appointment appointment = Appointment.builder()
                .idNumber("1134e1")
                .dateTime(OffsetDateTime.of(2023, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .doctor(buildDoctor())
                .patient(buildPatient().withAddress(address).withDiseases(Set.of()))
                .note(newNote)
                .build();
        AppointmentEntity appointmentEntity = AppointmentEntity.builder()
                .idNumber("1134e1")
                .dateTime(OffsetDateTime.of(2023, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .note("Old note")
                .build();
        Mockito.when(appointmentJpaRepository.findByIdNumber(appointment.getIdNumber()))
                .thenReturn(appointmentEntity);
        appointmentEntity = appointmentEntity.withNote(newNote);
        Mockito.when(appointmentJpaRepository.saveAndFlush(appointmentEntity))
                .thenReturn(appointmentEntity);

        // when
        Appointment result = appointmentRepository.update(appointment);

        // then
        Mockito.verify(appointmentJpaRepository, Mockito.times(1)).saveAndFlush(Mockito.any());
        Assertions.assertEquals(newNote, result.getNote());
    }

    @Test
    void canBeDeletedByItsId() {
        // given
        String idNumber = "1134e1";
        Mockito.doNothing().when(appointmentJpaRepository).deleteByIdNumber(idNumber);

        // when
        appointmentRepository.deleteByIdNumber(idNumber);

        // then
        Mockito.verify(appointmentJpaRepository, Mockito.times(1)).deleteByIdNumber(Mockito.any());
    }

    private List<Appointment> buildAppointmentsList() {
        Appointment appointment1 = Appointment.builder()
                .appointmentId(1)
                .idNumber("1134e1")
                .dateTime(OffsetDateTime.of(2023, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .doctor(null)
                .patient(null)
                .note("Testing note")
                .build();
        Appointment appointment2 = Appointment.builder()
                .appointmentId(2)
                .idNumber("1123d1")
                .dateTime(OffsetDateTime.of(2027, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .doctor(null)
                .patient(null)
                .build();
        return List.of(appointment1, appointment2);
    }

    private List<AppointmentEntity> buildAppointmentEntitiesList() {
        AppointmentEntity appointment1 = AppointmentEntity.builder()
                .appointmentId(1)
                .idNumber("1134e1")
                .dateTime(OffsetDateTime.of(2023, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .doctor(null)
                .patient(null)
                .note("Testing note")
                .build();
        AppointmentEntity appointment2 = AppointmentEntity.builder()
                .appointmentId(2)
                .idNumber("1123d1")
                .dateTime(OffsetDateTime.of(2027, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .doctor(null)
                .patient(null)
                .build();
        return List.of(appointment1, appointment2);
    }

    private DoctorEntity buildDoctorEntity() {
        return DoctorEntity.builder()
                .idNumber("4653")
                .name("Adam")
                .surname("Lekarski")
                .specialization("dermathologist")
                .calendar(Set.of(buildCalendarEntity()))
                .build();
    }

    private CalendarEntity buildCalendarEntity() {
        return CalendarEntity.builder()
                .calendarId(1)
                .dateTime(OffsetDateTime.of(2027, 6, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .doctor(null)
                .build();
    }

    private PatientEntity buildPatientEntity() {
        return PatientEntity.builder()
                .pesel("11122233344")
                .name("Adam")
                .surname("Pacjentowy")
                .phone("+48 123 456 789")
                .email("adam@pacjentowy.pl")
                .build();
    }

    private Doctor buildDoctor() {
        return Doctor.builder()
                .idNumber("4653")
                .name("Adam")
                .surname("Lekarski")
                .specialization("dermathologist")
                .calendar(Set.of(buildCalendar()))
                .build();
    }

    private Calendar buildCalendar() {
        return Calendar.builder()
                .calendarId(1)
                .dateTime(OffsetDateTime.of(2027, 6, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .doctor(null)
                .build();
    }

    private Patient buildPatient() {
        return Patient.builder()
                .pesel("11122233344")
                .name("Adam")
                .surname("Pacjentowy")
                .phone("+48 123 456 789")
                .email("adam@pacjentowy.pl")
                .build();
    }
}

