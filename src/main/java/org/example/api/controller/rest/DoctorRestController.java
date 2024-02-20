package org.example.api.controller.rest;

import lombok.AllArgsConstructor;
import org.example.api.dto.AppointmentDTO;
import org.example.api.dto.mapper.AppointmentMapper;
import org.example.business.AppointmentService;
import org.example.domain.exception.ProcessingException;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(DoctorRestController.API_DOCTOR)
public class DoctorRestController {

    public static final String API_DOCTOR = "/api/doctor";
    public static final String DOCTOR_APPOINTMENTS = "/{idNumber}";
    public static final String DOCTOR_APPOINTMENT_EDIT = "/{doctorId}/{appointmentId}/edit";


    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    @GetMapping(value = DOCTOR_APPOINTMENTS)
    public List<AppointmentDTO> doctorAppointments(
            @PathVariable String idNumber
    ) {
        return getAvailableAppointmentsByDoctorId(idNumber);
    }

    @PutMapping(value = DOCTOR_APPOINTMENT_EDIT)
    public AppointmentDTO editNoteOfAppointment(
            @PathVariable String doctorId,
            @PathVariable String appointmentId,
            @RequestParam String newNote
    ) {
        List<AppointmentDTO> availableAppointments = getAvailableAppointmentsByDoctorId(doctorId).stream()
                .filter(appointmentDTO -> appointmentDTO.getDateTime().isBefore(OffsetDateTime.now()))
                .toList();
        AppointmentDTO appointmentDTO = appointmentMapper.map(appointmentService.findByIdNumber(appointmentId));
        if (!availableAppointments.contains(appointmentDTO)) {
            throw new ProcessingException("This appointment is not assigned to this doctor");
        }
        appointmentDTO = appointmentDTO.withNote(newNote);
        appointmentService.update(appointmentMapper.map(appointmentDTO));
        return appointmentDTO;
    }



    private List<AppointmentDTO> getAvailableAppointmentsByDoctorId(String doctorId) {
        return appointmentService.findAvailableByDoctorsId(doctorId).stream()
                .map(appointmentMapper::map)
                .toList();
    }


}
