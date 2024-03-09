package org.example.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.example.infrastructure.database.entity.CalendarEntity;
import org.example.infrastructure.database.entity.DoctorEntity;
import org.example.integration.configuration.PersistenceContainerTestConfiguration;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CalendarJpaRepositoryTest {

    private CalendarJpaRepository calendarJpaRepository;
    private DoctorJpaRepository doctorJpaRepository;

    @Test
    void canNewCalendarBeAdded() {
        // given
        DoctorEntity doctorEntity = doctorJpaRepository.findByIdNumber("1111").get();
        CalendarEntity calendarEntity = CalendarEntity.builder()
                .dateTime(OffsetDateTime.now())
                .doctor(doctorEntity)
                .build();
        calendarJpaRepository.saveAndFlush(calendarEntity);
        // when
        List<CalendarEntity> result = calendarJpaRepository.findAll();

        // then
        assertThat(result).hasSize(5);

    }

}
