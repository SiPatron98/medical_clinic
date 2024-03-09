package org.example.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.example.infrastructure.database.entity.AppointmentEntity;
import org.example.infrastructure.database.entity.DoctorEntity;
import org.example.infrastructure.database.entity.PatientEntity;
import org.example.integration.configuration.PersistenceContainerTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AppointmentJpaRepositoryTest {

    private AppointmentJpaRepository appointmentJpaRepository;
    private DoctorJpaRepository doctorJpaRepository;
    private PatientJpaRepository patientJpaRepository;

    @Test
    void canAppointmentBeFoundByDoctorId() {
        // given
        String idNumber = "1111";

        // when
        Set<AppointmentEntity> availableByDoctorsId = appointmentJpaRepository.findAvailableByDoctorsId(idNumber);

        // then
        assertThat(availableByDoctorsId).hasSize(2);
    }

    @Test
    void canAppointmentBeFoundByPatientPesel() {
        // given
        String pesel = "83011863727";

        // when
        Set<AppointmentEntity> availableByPatientsPesel = appointmentJpaRepository.findAvailableByPatientsPesel(pesel);

        // then
        assertThat(availableByPatientsPesel).hasSize(1);
    }

    @Test
    void canAppointmentBeFoundByItsId() {
        // given
        String idAppointment = "001";

        // when
        AppointmentEntity appointmentEntity = appointmentJpaRepository.findByIdNumber(idAppointment);

        // then
        assertThat(appointmentEntity).hasFieldOrPropertyWithValue("dateTime", OffsetDateTime.of(
                LocalDate.of(2023, 12, 18), LocalTime.of(14, 0), ZoneOffset.ofHours(1))
        );
    }

    @Test
    void canAppointmentBeDeletedByItsId() {
        // given
        String idAppointment = "001";

        // when
        appointmentJpaRepository.deleteByIdNumber(idAppointment);

        // then
        List<AppointmentEntity> result = appointmentJpaRepository.findAll();
        assertThat(result).hasSize(3);
    }

    @Test
    void canMakeAnAppointment() {
        // given
        Optional<DoctorEntity> doctorEntity = doctorJpaRepository.findByIdNumber("1111");
        Optional<PatientEntity> patientEntity = patientJpaRepository.findByPesel("54073957136");
        AppointmentEntity appointmentEntity = AppointmentEntity.builder()
                .idNumber("005")
                .dateTime(OffsetDateTime.now())
                .doctor(doctorEntity.get())
                .patient(patientEntity.get())
                .build();

        // when
        appointmentJpaRepository.saveAndFlush(appointmentEntity);

        // then
        List<AppointmentEntity> result = appointmentJpaRepository.findAll();
        assertThat(result).hasSize(5);
    }

    @Test
    void canUpdateAnAppointment() {
        // given
        String appointmentId = "001";
        String newNote = "New note";
        AppointmentEntity appointmentEntity = appointmentJpaRepository.findByIdNumber(appointmentId);

        // when, them
        assertThat(appointmentEntity).hasFieldOrPropertyWithValue("note", "Grypa - przepisano antybiotyk");
        appointmentEntity.setNote(newNote);
        appointmentJpaRepository.saveAndFlush(appointmentEntity);

        AppointmentEntity updatedEntity = appointmentJpaRepository.findByIdNumber(appointmentId);
        assertThat(updatedEntity).hasFieldOrPropertyWithValue("note", newNote);
    }



}
