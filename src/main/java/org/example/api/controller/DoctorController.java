package org.example.api.controller;

import lombok.AllArgsConstructor;
import org.example.api.dto.AppointmentDTO;
import org.example.api.dto.DiseaseDTO;
import org.example.api.dto.DoctorDTO;
import org.example.api.dto.UpdateAppointmentDTO;
import org.example.api.dto.mapper.AppointmentMapper;
import org.example.api.dto.mapper.DiseaseMapper;
import org.example.api.dto.mapper.DoctorMapper;
import org.example.business.AppointmentService;
import org.example.business.DoctorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/doctor")
@AllArgsConstructor
public class DoctorController {

    static final String DOCTOR = "/doctor";
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;
    private final DiseaseMapper diseaseMapper;

    @GetMapping
    public String doctorPanelPage(
            @RequestParam(value = "idNumber", required = false) String idNumber,
            Model model
    ) {
        var allIdNumbers = getAvailableDoctors().stream()
                .map(DoctorDTO::getIdNumber)
                .toList();
        var allDoctorAppointments = appointmentService.findAvailableByDoctorsId(idNumber).stream()
                .map(appointmentMapper::map)
                .collect(Collectors.toSet());
        var futureDoctorAppointments = allDoctorAppointments.stream()
                .filter(appointmentDTO -> appointmentDTO.getDateTime().isAfter(OffsetDateTime.now()))
                .collect(Collectors.toSet());
        var pastDoctorAppointments = allDoctorAppointments.stream()
                .filter(appointmentDTO -> !appointmentDTO.getDateTime().isAfter(OffsetDateTime.now()))
                .collect(Collectors.toSet());
        DoctorDTO doctor;
        if (Objects.nonNull(idNumber)) {
            doctor = doctorMapper.map(doctorService.findDoctor(idNumber));
        } else {
            doctor = doctorMapper.map(doctorService.findDoctor("1111"));
        }
        model.addAttribute("allIdNumbers", allIdNumbers);
        model.addAttribute("allDoctorAppointments", allDoctorAppointments);
        model.addAttribute("idDoctorNumber", idNumber);
        model.addAttribute("futureDoctorAppointments", futureDoctorAppointments);
        model.addAttribute("pastDoctorAppointments", pastDoctorAppointments);
        model.addAttribute("doctor", doctor);
        return "doctor";
    }

    @GetMapping("details/{appointmentId}")
    public String showAppointmentDetails(
            @PathVariable String appointmentId,
            Model model
    ) {
        AppointmentDTO appointmentDTO = appointmentMapper.map(appointmentService.findByIdNumber(appointmentId));
        String note = appointmentDTO.getNote();
        Set<DiseaseDTO> patientDiseases = appointmentDTO.getPatient().getDiseases().stream()
                .map(diseaseMapper::map)
                .collect(Collectors.toSet());
        model.addAttribute("note", note);
        model.addAttribute("patientDiseases", patientDiseases);
        model.addAttribute("appointmentDTO", appointmentDTO);
        return "appointment_details";
    }

    @GetMapping("/edit/{appointmentId}")
    public String showEditAppointmentNote(
            @PathVariable("appointmentId") String appointmentId,
            Model model
    ) {
        model.addAttribute("updateAppointmentDTO", new UpdateAppointmentDTO().withIdNumber(appointmentId));
        model.addAttribute("appointmentId", appointmentId);
        return "appointment_edit";
    }

    @PutMapping("/edit/{appointmentId}")
    public String editAppointmentNote(
            @ModelAttribute("updateAppointmentDTO") UpdateAppointmentDTO updateAppointmentDTO,
            @PathVariable("appointmentId") String appointmentId,
            Model model
    ) {
        AppointmentDTO appointmentDTO = appointmentMapper.map(appointmentService.findByIdNumber(updateAppointmentDTO.getIdNumber()));
        appointmentDTO = appointmentDTO.withNote(updateAppointmentDTO.getNote());
        appointmentService.update(appointmentMapper.map(appointmentDTO));
        return "redirect:/doctor";
    }

    private List<DoctorDTO> getAvailableDoctors() {
        return doctorService.findAvailable().stream()
                .map(doctorMapper::map)
                .toList();

    }
}
