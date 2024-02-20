package org.example.api.dto.mapper;

import org.example.api.dto.AppointmentDTO;
import org.example.domain.Appointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    AppointmentDTO map(final Appointment appointment);

    Appointment map(final AppointmentDTO appointmentDTO);
}
