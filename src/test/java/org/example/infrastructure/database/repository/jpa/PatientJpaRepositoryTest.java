package org.example.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.example.domain.Patient;
import org.example.infrastructure.database.entity.PatientEntity;
import org.example.integration.configuration.PersistenceContainerTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PatientJpaRepositoryTest {

    private PatientJpaRepository patientJpaRepository;

    @Test
    void isPatientCanBeFoundByPesel() {
        // given
        String pesel = "52070997836";

        // when
        Optional<PatientEntity> patientEntityOptional = patientJpaRepository.findByPesel(pesel);

        //then
        assertThat(patientEntityOptional).get().hasFieldOrProperty("pesel");

    }

    @Test
    void canAllPatientsCanBeFoundAndAddANewOne() {
        // given
        PatientEntity patientEntity = PatientEntity.builder()
                .pesel("11122233344")
                .name("Adam")
                .surname("Pacjentowy")
                .phone("+48 123 456 789")
                .email("adam@pacjentowy.pl")
                .build();
        patientJpaRepository.saveAndFlush(patientEntity);

        // when
        List<PatientEntity> result = patientJpaRepository.findAll();

        //then
        assertThat(result).hasSize(9);

    }



}
