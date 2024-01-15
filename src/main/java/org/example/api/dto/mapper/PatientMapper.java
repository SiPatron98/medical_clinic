package org.example.api.dto.mapper;

import org.example.api.dto.DoctorDTO;
import org.example.api.dto.PatientDTO;
import org.example.domain.Doctor;
import org.example.domain.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientDTO map(final Patient patient);

    Patient map(final PatientDTO patientDTO);
}
