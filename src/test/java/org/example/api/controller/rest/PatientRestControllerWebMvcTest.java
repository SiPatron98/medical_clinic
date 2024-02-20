package org.example.api.controller.rest;

import lombok.AllArgsConstructor;
import org.example.api.dto.AppointmentDTO;
import org.example.api.dto.DoctorDTO;
import org.example.api.dto.PatientDTO;
import org.example.api.dto.mapper.AppointmentMapper;
import org.example.business.AppointmentService;
import org.example.domain.Appointment;
import org.hamcrest.Matchers;
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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PatientRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PatientRestControllerWebMvcTest {

    MockMvc mockMvc;

    @MockBean
    private final AppointmentService appointmentService;

    @MockBean
    private final AppointmentMapper appointmentMapper;

    @Test
    void arePatientAppointmentsWorksCorrectly() throws Exception {
        // given
        PatientDTO patientDTO = PatientDTO.buildDefaultPatient();
        Set<Appointment> appointments = buildAppointmentsSet();
        String pesel = patientDTO.getPesel();
        Appointment appointment  = Appointment.builder()
                .appointmentId(1)
                .idNumber("1134e1")
                .dateTime(OffsetDateTime.now())
                .doctor(null)
                .patient(null)
                .build();
        Appointment appointment2  = Appointment.builder()
                .appointmentId(1)
                .idNumber("1123d1")
                .dateTime(OffsetDateTime.now())
                .doctor(null)
                .patient(null)
                .build();

        // when, then
        Mockito.when(appointmentService.findAvailableByPatientsPesel(pesel))
                .thenReturn(appointments);

        Mockito.when(appointmentMapper.map(appointment)).thenReturn(buildAppointmentsDTOList().get(0));
        Mockito.when(appointmentMapper.map(appointment2)).thenReturn(buildAppointmentsDTOList().get(1));


        mockMvc.perform(get(PatientRestController.API_PATIENT + PatientRestController.PATIENT_APPOINTMENTS, pesel))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andReturn();
    }

    @Test
    void isDeletingAppointmentWorksCorrectly() throws Exception {
        // given
        AppointmentDTO appointmentDTO = buildAppointmentsDTOList().get(0);
        String idNumber = appointmentDTO.getIdNumber();
        Appointment appointment = Appointment.builder()
                .appointmentId(1)
                .idNumber("1134e1")
                .dateTime(OffsetDateTime.of(2025, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .doctor(null)
                .patient(null)
                .build();

        // when, then
        Mockito.when(appointmentService.findByIdNumber(idNumber))
                .thenReturn(appointment);
        Mockito.when(appointmentMapper.map(appointment))
                .thenReturn(appointmentDTO);
        Mockito.doNothing().when(appointmentService).deleteByIdNumber(idNumber);


        mockMvc.perform(delete(PatientRestController.API_PATIENT + PatientRestController.DELETE_APPOINTMENT, idNumber))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idNumber").value(idNumber))
                .andReturn();
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

    private List<AppointmentDTO> buildAppointmentsDTOList() {
        AppointmentDTO appointmentDTO1 = AppointmentDTO.builder()
                .appointmentId(1)
                .idNumber("1134e1")
                .dateTime(OffsetDateTime.of(2025, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
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


}
