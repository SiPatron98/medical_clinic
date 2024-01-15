package org.example.infrastructure.database.repository.jpa;

import org.example.infrastructure.database.entity.CalendarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarJpaRepository extends JpaRepository<CalendarEntity, Integer> {
}
