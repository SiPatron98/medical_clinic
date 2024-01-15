package org.example.api.controller;

import lombok.AllArgsConstructor;
import org.example.api.dto.PatientDTO;
import org.example.api.dto.mapper.AppointmentMapper;
import org.example.api.dto.mapper.PatientMapper;
import org.example.business.AppointmentService;
import org.example.business.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class PatientController {

    static final String PATIENT = "/patient";
    private final PatientService patientService;
    private final PatientMapper patientMapper;
    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    @GetMapping(value = PATIENT)
    public String patientPanelPage(
            @RequestParam(value = "pesel", required = false) String pesel,
            Model model
    ) {
        var allPesels = getAvailablePatients().stream()
                .map(PatientDTO::getPesel)
                .toList();
        var allPatientAppointments = appointmentService.findAvailableByPatientsPesel(pesel).stream()
                .map(appointmentMapper::map)
                .collect(Collectors.toSet());
        var futurePatientAppointments = allPatientAppointments.stream()
                .filter(appointmentDTO -> appointmentDTO.getDateTime().isAfter(OffsetDateTime.now()))
                .collect(Collectors.toSet());
        var pastPatientAppointments = allPatientAppointments.stream()
                .filter(appointmentDTO -> !appointmentDTO.getDateTime().isAfter(OffsetDateTime.now()))
                .collect(Collectors.toSet());
        PatientDTO patient;
        if (Objects.nonNull(pesel)) {
            patient = patientMapper.map(patientService.findPatient(pesel));
        } else {
            patient = patientMapper.map(patientService.findPatient("52070997836"));
        }
        model.addAttribute("allPesels", allPesels);
        model.addAttribute("allPatientAppointments", allPatientAppointments);
        model.addAttribute("pesel", pesel);
        model.addAttribute("futurePatientAppointments", futurePatientAppointments);
        model.addAttribute("pastPatientAppointments", pastPatientAppointments);
        model.addAttribute("patient", patient);
        return "patient";
    }

    private List<PatientDTO> getAvailablePatients() {
        return patientService.findAvailable().stream()
                .map(patientMapper::map)
                .toList();
    }
}
