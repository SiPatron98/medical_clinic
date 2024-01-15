package org.example.infrastructure.database.repository.mapper;

import org.example.domain.Calendar;
import org.example.domain.Doctor;
import org.example.infrastructure.database.entity.CalendarEntity;
import org.example.infrastructure.database.entity.DoctorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DoctorEntityMapper {

    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "calendar", ignore = true)
    Doctor mapFromEntity(DoctorEntity entity);

    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "calendar", ignore = true)
    DoctorEntity mapToEntity(Doctor doctor);

    default Doctor mapFromEntityWithCalendar(DoctorEntity entity) {
        return mapFromEntity(entity).withCalendar(mapCalendarFromEntity(entity.getCalendar()));
    }

    default Set<Calendar> mapCalendarFromEntity(Set<CalendarEntity> calendarEntities) {
        Set<Calendar> calendarSet = new HashSet<>();
        calendarEntities.forEach(
                calendarEntity -> calendarSet.add(Calendar.builder()
                        .calendarId(calendarEntity.getCalendarId())
                        .dateTime(calendarEntity.getDateTime())
                        .build())
        );
        return calendarSet;
    }
}
