package org.example.api.controller.rest;


import lombok.AllArgsConstructor;
import org.example.api.dto.AppointmentDTO;
import org.example.api.dto.DoctorDTO;
import org.example.api.dto.mapper.AppointmentMapper;
import org.example.business.AppointmentService;
import org.example.domain.Appointment;
import org.example.domain.Doctor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DoctorRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DoctorRestControllerWebMvcTest {

    private MockMvc mockMvc;

    @MockBean
    private final AppointmentService appointmentService;

    @MockBean
    private final AppointmentMapper appointmentMapper;

    @Test
    void areDoctorAppointmentsWorksCorrectly() throws Exception {
        // given
        DoctorDTO doctor = DoctorDTO.buildDefaultDoctor();
        Set<Appointment> appointments = buildAppointmentsSet();
        String idNumber = doctor.getIdNumber();
        Appointment appointment  = Appointment.builder()
                .appointmentId(1)
                .idNumber("1134e1")
                .dateTime(OffsetDateTime.now())
                .doctor(null)
                .patient(null)
                .build();
        Appointment appointment2  = Appointment.builder()
                .appointmentId(2)
                .idNumber("1123d1")
                .dateTime(OffsetDateTime.now())
                .doctor(null)
                .patient(null)
                .build();

        // when, then
        Mockito.when(appointmentService.findAvailableByDoctorsId(idNumber))
                        .thenReturn(appointments);

        Mockito.when(appointmentMapper.map(appointment))
                .thenReturn(buildAppointmentsDTOList().get(0));
        Mockito.when(appointmentMapper.map(appointment2))
                .thenReturn(buildAppointmentsDTOList().get(1));


        mockMvc.perform(get(DoctorRestController.API_DOCTOR + DoctorRestController.DOCTOR_APPOINTMENTS, idNumber))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andReturn();
    }

    @Test
    void isPuttingNewNoteWorksCorrectly() throws Exception {
        // given
        DoctorDTO doctorDTO = DoctorDTO.buildDefaultDoctor();
        Doctor doctor = Doctor.builder()
                .idNumber("4653")
                .name("Adam")
                .surname("Lekarski")
                .specialization("dermathologist")
                .calendar(Set.of())
                .build();
        AppointmentDTO appointmentDTO = AppointmentDTO.builder()
                .appointmentId(1)
                .idNumber("1134e1")
                .dateTime(OffsetDateTime.of(2023, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .note("Old note")
                .doctor(doctor)
                .patient(null)
                .build();
        Appointment appointment = Appointment.builder()
                .appointmentId(1)
                .idNumber("1134e1")
                .dateTime(OffsetDateTime.of(2023, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .note("Old note")
                .doctor(doctor)
                .patient(null)
                .build();
        String oldNote = "Old note";
        String newNote = "New note";
        // when, then
        Mockito.when(appointmentService.findAvailableByDoctorsId(doctorDTO.getIdNumber()))
                .thenReturn(Set.of(appointment));
        Mockito.when(appointmentService.findByIdNumber(appointment.getIdNumber()))
                        .thenReturn(appointment);
        Mockito.when(appointmentMapper.map(appointment))
                .thenReturn(appointmentDTO);
        Assertions.assertEquals(appointmentDTO.getNote(), oldNote);

        appointmentDTO = appointmentDTO.withNote(newNote);

        Mockito.when(appointmentService.update(appointment))
                .thenReturn(appointment);

        mockMvc.perform(put(DoctorRestController.API_DOCTOR + DoctorRestController.DOCTOR_APPOINTMENT_EDIT,
                        doctorDTO.getIdNumber(), appointmentDTO.getIdNumber()).param("newNote", newNote))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idNumber").value("1134e1"))
                .andReturn();
    }


    private List<AppointmentDTO> buildAppointmentsDTOList() {
        AppointmentDTO appointmentDTO1 = AppointmentDTO.builder()
                .appointmentId(1)
                .idNumber("1134e1")
                .dateTime(OffsetDateTime.now())
                .doctor(null)
                .patient(null)
                .build();
        AppointmentDTO appointmentDTO2 = AppointmentDTO.builder()
                .appointmentId(2)
                .idNumber("1123d1")
                .dateTime(OffsetDateTime.now())
                .doctor(null)
                .patient(null)
                .build();
        return List.of(appointmentDTO1, appointmentDTO2);
    }

    private Set<Appointment> buildAppointmentsSet() {
        Appointment appointment1 = Appointment.builder()
                .appointmentId(1)
                .idNumber("1134e1")
                .dateTime(OffsetDateTime.now())
                .doctor(null)
                .patient(null)
                .build();
        Appointment appointment2 = Appointment.builder()
                .appointmentId(2)
                .idNumber("1123d1")
                .dateTime(OffsetDateTime.now())
                .doctor(null)
                .patient(null)
                .build();
        return Set.of(appointment1, appointment2);
    }


}
