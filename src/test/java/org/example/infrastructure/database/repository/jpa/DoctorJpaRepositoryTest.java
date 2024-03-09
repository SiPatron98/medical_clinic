package org.example.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.example.infrastructure.database.entity.DoctorEntity;
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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DoctorJpaRepositoryTest {

    private DoctorJpaRepository doctorJpaRepository;

    @Test
    void isDoctorCanBeFoundByIdNumber() {
        // given
        String idNumber = "1111";

        // when
        Optional<DoctorEntity> doctorEntityOptional = doctorJpaRepository.findByIdNumber(idNumber);

        //then
        assertThat(doctorEntityOptional).get().hasFieldOrProperty("idNumber");

    }

    @Test
    void canAllDoctorsCanBeFoundAndAddANewOne() {
        // given
        DoctorEntity doctorEntity = DoctorEntity.builder()
                .idNumber("4653")
                .name("Adam")
                .surname("Lekarski")
                .specialization("dermathologist")
                .calendar(Set.of())
                .build();
        doctorJpaRepository.saveAndFlush(doctorEntity);

        // when
        List<DoctorEntity> result = doctorJpaRepository.findAll();

        //then
        assertThat(result).hasSize(5);

    }
}
