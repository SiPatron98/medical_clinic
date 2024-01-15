package org.example.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.example.business.dao.CalendarDAO;
import org.example.domain.Calendar;
import org.example.infrastructure.database.repository.jpa.CalendarJpaRepository;
import org.springframework.stereotype.Repository;
import org.example.infrastructure.database.repository.mapper.CalendarEntityMapper;

@Repository
@AllArgsConstructor
public class CalendarRepository implements CalendarDAO {

    private final CalendarJpaRepository calendarJpaRepository;
    private final CalendarEntityMapper calendarEntityMapper;

    @Override
    public void addNewDate(Calendar calendar) {
        calendarJpaRepository.saveAndFlush(calendarEntityMapper.map(calendar));
    }
}
