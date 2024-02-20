package org.example.api.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    static final String ADMIN = "/admin";
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
    private final PatientService patientService;
    private final PatientMapper patientMapper;

    @GetMapping
    public ModelAndView adminPanelPage() {
        Map<String, ?> data = prepareNecessaryData();
        return new ModelAndView("admin", data);
    }

    @PostMapping("/add_doctor")
    public String addDoctor(
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

        return "redirect:/admin";
    }

    @PostMapping("/add_patient")
    public String addPatient(
            @Valid @ModelAttribute("patientDTO") PatientDTO patientDTO,
            @RequestParam(value = "pesel") String pesel,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "surname") String surname,
            @RequestParam(value = "phone") String phone,
            @RequestParam(value = "email") String email
    ) {
        patientDTO = PatientDTO.builder()
                .pesel(pesel)
                .name(name)
                .surname(surname)
                .phone(phone)
                .email(email)
                .build();
        Patient patient = patientMapper.map(patientDTO);
        patientService.save(patient);
        return "redirect:/admin";
    }

    private Map<String, ?> prepareNecessaryData() {
        var availableDoctors = getAvailableDoctors();
        var availablePatients = getAvailablePatients();
        return Map.of(
                "availableDoctorDTOs", availableDoctors,
                "availablePatientDTOs", availablePatients
        );
    }

    private List<PatientDTO> getAvailablePatients() {
        return patientService.findAvailable().stream()
                .map(patientMapper::map)
                .toList();
    }

    private List<DoctorDTO> getAvailableDoctors() {
        return doctorService.findAvailable().stream()
                .map(doctorMapper::map)
                .toList();

    }
}
