package org.example.api.controller.rest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.api.dto.DoctorDTO;
import org.example.api.dto.PatientDTO;
import org.example.api.dto.mapper.DoctorMapper;
import org.example.api.dto.mapper.PatientMapper;
import org.example.business.DoctorService;
import org.example.business.PatientService;
import org.example.domain.Doctor;
import org.example.domain.Patient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(AdminRestController.API_ADMIN)
public class AdminRestController {
    public static final String API_ADMIN = "/api/admin";
    public static final String NEW_DOCTOR = "/api/admin/new_doctor";
    public static final String NEW_PATIENT = "/api/admin/new_patient";
    public static final String ALL_PATIENTS = "/api/admin/patients";
    public static final String ALL_DOCTORS = "/api/admin/doctors";

    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
    private final PatientService patientService;
    private final PatientMapper patientMapper;

    @GetMapping(value = ALL_DOCTORS)
    public List<DoctorDTO> findAllDoctors(){
        return doctorService.findAvailable().stream()
                .map(doctorMapper::map)
                .toList();
    }

    @GetMapping(value = ALL_PATIENTS)
    public List<PatientDTO> findAllPatients(){
        return patientService.findAvailable().stream()
                .map(patientMapper::map)
                .toList();
    }

    @PostMapping(value = NEW_DOCTOR)
    public DoctorDTO addNewDoctor(
            @RequestParam(value = "idNumber") String idNumber,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "surname") String surname,
            @RequestParam(value = "specialization") String specialization
    ) {
        DoctorDTO doctorDTO = DoctorDTO.builder()
                .idNumber(idNumber)
                .name(name)
                .surname(surname)
                .specialization(specialization)
                .build();
        Doctor doctor = doctorMapper.map(doctorDTO);
        doctorService.save(doctor);
        return doctorDTO;
    }

    @PostMapping(value = NEW_PATIENT)
    public PatientDTO addNewPatient(
            @Valid @RequestBody PatientDTO patientDTO
    ) {
        Patient patient = patientMapper.map(patientDTO);
        patientService.save(patient);
        return patientDTO;
    }


}
