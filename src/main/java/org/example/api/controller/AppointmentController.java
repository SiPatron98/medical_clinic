package org.example.api.controller;

import lombok.AllArgsConstructor;
import org.example.api.dto.AppointmentDTO;
import org.example.api.dto.DoctorDTO;
import org.example.api.dto.PatientDTO;
import org.example.api.dto.mapper.DoctorMapper;
import org.example.api.dto.mapper.PatientMapper;
import org.example.business.AppointmentService;
import org.example.business.DoctorService;
import org.example.business.PatientService;
import org.example.domain.Appointment;
import org.example.domain.Doctor;
import org.example.domain.Patient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/new_appointment")
@AllArgsConstructor
public class AppointmentController {

    private final PatientService patientService;
    private final PatientMapper patientMapper;
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
    private final AppointmentService appointmentService;

    @GetMapping
    public String appointmentPanelPage(
            Model model
    ) {
        var allPesels = getAvailablePatients().stream()
                .map(PatientDTO::getPesel)
                .toList();
        var doctorIds = getAvailableDoctors().stream()
                .map(DoctorDTO::getIdNumber)
                .toList();
        List<Integer> years = List.of(2024, 2025, 2026);

        Map<String, Integer> mapOfDaysAndMonths = Map.ofEntries(
                Map.entry("January", 31),
                Map.entry("February", 28),
                Map.entry("March", 31),
                Map.entry("April", 30),
                Map.entry("May", 31),
                Map.entry("June", 30),
                Map.entry("July", 31),
                Map.entry("August", 31),
                Map.entry("September", 30),
                Map.entry("October", 31),
                Map.entry("November", 30),
                Map.entry("December", 31)
        );

        Map<String, Integer> mapOfDaysAndMonthsLeapYear = Map.ofEntries(
                Map.entry("January", 31),
                Map.entry("February", 29),
                Map.entry("March", 31),
                Map.entry("April", 30),
                Map.entry("May", 31),
                Map.entry("June", 30),
                Map.entry("July", 31),
                Map.entry("August", 31),
                Map.entry("September", 30),
                Map.entry("October", 31),
                Map.entry("November", 30),
                Map.entry("December", 31)
        );


        model.addAttribute("allPesels", allPesels);
        model.addAttribute("doctorIds", doctorIds);
        model.addAttribute("years", years);
        model.addAttribute("mapOfDaysAndMonths", mapOfDaysAndMonths);
        model.addAttribute("mapOfDaysAndMonthsLeapYear", mapOfDaysAndMonthsLeapYear);
        return "new_appointment";
    }

    @PostMapping("/new")
    public String makeAnAppointment(
            @RequestParam(value = "doctorId") String doctorId,
            @RequestParam(value = "pesel") String pesel,
            @RequestParam(value = "year") Integer year,
            @RequestParam(value = "month") Integer month,
            @RequestParam(value = "day") Integer day,
            @RequestParam(value = "hour") Integer hour
    ) {
        Doctor doctor = doctorService.findDoctor(doctorId);
        Patient patient = patientService.findPatient(pesel);
        OffsetDateTime dateOfAppointment = OffsetDateTime.of(year, month, day, hour, 0, 0, 0, ZoneOffset.UTC);
        Appointment appointment = Appointment.builder()
                .dateTime(dateOfAppointment)
                .doctor(doctor)
                .patient(patient)
                .build();
        appointmentService.makeAnAppointment(appointment);

        return "redirect:/success_appointment";
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
