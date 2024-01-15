package org.example.api.dto.mapper;

import org.example.api.dto.DoctorDTO;
import org.example.domain.Doctor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    DoctorDTO map(final Doctor doctor);

    Doctor map(final DoctorDTO doctorDTO);
}
