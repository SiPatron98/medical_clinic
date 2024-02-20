package org.example.api.controller.rest;

import lombok.AllArgsConstructor;
import org.example.api.dto.AppointmentDTO;
import org.example.api.dto.mapper.AppointmentMapper;
import org.example.business.AppointmentService;
import org.example.domain.Appointment;
import org.example.domain.exception.ProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping(PatientRestController.API_PATIENT)
public class PatientRestController {

    public static final String API_PATIENT = "/api/patient";
    public static final String PATIENT_APPOINTMENTS = "/{pesel}";
    public static final String DELETE_APPOINTMENT = "/{appointmentId}";

    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    @GetMapping(value = PATIENT_APPOINTMENTS)
    public List<AppointmentDTO> patientAppointments(
            @PathVariable String pesel
    ) {
        return getAvailableAppointmentsByPatientPesel(pesel);
    }

    @DeleteMapping(value = DELETE_APPOINTMENT)
    public ResponseEntity<AppointmentDTO> deleteAppointment(
            @PathVariable String appointmentId
    ) {
        Appointment appointment = appointmentService.findByIdNumber(appointmentId);
        AppointmentDTO appointmentDTO = appointmentMapper.map(appointment);
        if (appointmentDTO.getDateTime().isBefore(OffsetDateTime.now())) {
            throw new ProcessingException("You can't delete past appointment");
        }
        appointmentService.deleteByIdNumber(appointmentId);

        if (Objects.isNull(appointmentId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity
                .ok(appointmentDTO);
    }

    private List<AppointmentDTO> getAvailableAppointmentsByPatientPesel(String pesel) {
        return appointmentService.findAvailableByPatientsPesel(pesel).stream()
                .map(appointmentMapper::map)
                .toList();
    }
}
