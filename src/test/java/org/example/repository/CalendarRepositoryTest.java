package org.example.repository;

import org.example.domain.Calendar;
import org.example.domain.Doctor;
import org.example.infrastructure.database.entity.CalendarEntity;
import org.example.infrastructure.database.entity.DoctorEntity;
import org.example.infrastructure.database.repository.CalendarRepository;
import org.example.infrastructure.database.repository.jpa.CalendarJpaRepository;
import org.example.infrastructure.database.repository.mapper.CalendarEntityMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class CalendarRepositoryTest {

    @InjectMocks
    private CalendarRepository calendarRepository;

    @Mock
    private CalendarJpaRepository calendarJpaRepository;
    @Mock
    private CalendarEntityMapper calendarEntityMapper;

    @Test
    void canAddANewDate() {
        // given
        Doctor doctor = Doctor.builder()
                .idNumber("4653")
                .name("Adam")
                .surname("Lekarski")
                .specialization("dermatologist")
                .calendar(Set.of())
                .build();
        DoctorEntity doctorEntity = DoctorEntity.builder()
                .idNumber("4653")
                .name("Adam")
                .surname("Lekarski")
                .specialization("dermatologist")
                .calendar(Set.of())
                .build();
        Calendar calendar = Calendar.builder()
                .calendarId(1)
                .doctor(doctor)
                .dateTime(OffsetDateTime.of(2027, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .build();

        CalendarEntity calendarEntity = CalendarEntity.builder()
                .calendarId(1)
                .doctor(doctorEntity)
                .dateTime(OffsetDateTime.of(2027, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .build();
        Mockito.when(calendarEntityMapper.mapToEntityWithDoctor(calendar))
                .thenReturn(calendarEntity);
        Mockito.when(calendarJpaRepository.saveAndFlush(calendarEntity))
                .thenReturn(calendarEntity);
        // when
        calendarRepository.addNewDate(calendar);

        // then
        Mockito.verify(calendarJpaRepository, Mockito.times(1)).saveAndFlush(Mockito.any());

    }
}
