package org.example.api.controller;


import lombok.AllArgsConstructor;
import org.example.api.dto.AppointmentDTO;
import org.example.api.dto.PatientDTO;
import org.example.api.dto.mapper.AppointmentMapper;
import org.example.api.dto.mapper.PatientMapper;
import org.example.business.AppointmentService;
import org.example.business.PatientService;
import org.example.domain.Appointment;
import org.example.domain.Doctor;
import org.example.domain.Patient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PatientController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PatientControllerWebMvcTest {

    private MockMvc mockMvc;

    @MockBean
    private final PatientService patientService;
    @MockBean
    private final PatientMapper patientMapper;
    @MockBean
    private final AppointmentService appointmentService;
    @MockBean
    private final AppointmentMapper appointmentMapper;

    @Test
    void isPatientPanelWorksCorrectly() throws Exception {
        // given
        LinkedMultiValueMap<String, String> paremeters = new LinkedMultiValueMap<>();
        Map<String, String> paramMap = PatientDTO.buildDefaultPatient().asMap();

        PatientDTO patientDTO = PatientDTO.buildDefaultPatient();
        Patient patient = Patient.builder()
                .pesel("11122233344")
                .name("Adam")
                .surname("Pacjentowy")
                .phone("+48 123 456 789")
                .email("adam@pacjentowy.pl")
                .build();
        List<AppointmentDTO> appointmentDTOS = buildAppointmentsDTOList();
        Set<Appointment> appointments = buildAppointmentsSet();
        paramMap.forEach(paremeters::add);
        int i = 0;

        // when, then
        Mockito.when(patientService.findAvailable())
                .thenReturn(List.of(patient));
        Mockito.when(patientMapper.map(patient))
                .thenReturn(patientDTO);
        Mockito.when(appointmentService.findAvailableByPatientsPesel(patient.getPesel()))
                .thenReturn(appointments);
        Mockito.when(patientService.findPatient(patient.getPesel()))
                .thenReturn(patient);

        for (Appointment appoint : appointments) {
            Mockito.when(appointmentMapper.map(appoint))
                    .thenReturn(appointmentDTOS.get(i));
            i++;
        }


        mockMvc.perform(get("/patient").params(paremeters))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("allPesels"))
                .andExpect(model().attributeExists("allPatientAppointments"))
                .andExpect(model().attributeExists("pesel"))
                .andExpect(model().attributeExists("futurePatientAppointments"))
                .andExpect(model().attributeExists("pastPatientAppointments"))
                .andExpect(model().attributeExists("patient"))
                .andExpect(view().name("patient"));
    }

    @Test
    void isDeletingAppointmentWorksCorrectly() throws Exception {
        // given
        Appointment appointment = Appointment.builder()
                .appointmentId(1)
                .idNumber("1134e1")
                .dateTime(OffsetDateTime.of(2028, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .doctor(buildDoctor())
                .patient(null)
                .build();


        // when, then
        Mockito.doNothing().when(appointmentService).deleteByIdNumber(appointment.getIdNumber());

        mockMvc.perform(delete("/patient/{appointmentId}", appointment.getIdNumber()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/patient"));
    }


    private List<AppointmentDTO> buildAppointmentsDTOList() {
        AppointmentDTO appointmentDTO1 = AppointmentDTO.builder()
                .appointmentId(1)
                .idNumber("1134e1")
                .dateTime(OffsetDateTime.of(2023, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .doctor(buildDoctor())
                .patient(null)
                .note("Testing note")
                .build();
        AppointmentDTO appointmentDTO2 = AppointmentDTO.builder()
                .appointmentId(2)
                .idNumber("1123d1")
                .dateTime(OffsetDateTime.of(2027, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .doctor(buildDoctor())
                .patient(null)
                .build();
        return List.of(appointmentDTO1, appointmentDTO2);
    }

    private Set<Appointment> buildAppointmentsSet() {
        Appointment appointment1 = Appointment.builder()
                .appointmentId(1)
                .idNumber("1134e1")
                .dateTime(OffsetDateTime.of(2023, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .doctor(buildDoctor())
                .patient(null)
                .build();
        Appointment appointment2 = Appointment.builder()
                .appointmentId(2)
                .idNumber("1123d1")
                .dateTime(OffsetDateTime.of(2027, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .doctor(buildDoctor())
                .patient(null)
                .build();
        return Set.of(appointment1, appointment2);
    }

    private Doctor buildDoctor() {
        return Doctor.builder()
                .idNumber("4653")
                .name("Adam")
                .surname("Lekarski")
                .specialization("dermathologist")
                .calendar(Set.of())
                .build();
    }

}
