package org.example.api.controller.rest;

import lombok.AllArgsConstructor;
import org.example.api.dto.AppointmentDTO;
import org.example.api.dto.mapper.AppointmentMapper;
import org.example.business.AppointmentService;
import org.example.business.DoctorService;
import org.example.business.PatientService;
import org.example.domain.Appointment;
import org.example.domain.Doctor;
import org.example.domain.Patient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AppointmentRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AppointmentRestControllerWebMvcTest {

    private MockMvc mockMvc;

    @MockBean
    private final DoctorService doctorService;
    @MockBean
    private final PatientService patientService;
    @MockBean
    private final AppointmentService appointmentService;
    @MockBean
    private final AppointmentMapper appointmentMapper;

    @Test
    void isNewAppointmentWorksCorrectly() throws Exception {
        // given
        Doctor doctor = Doctor.builder()
                .idNumber("4653")
                .name("Adam")
                .surname("Lekarski")
                .specialization("dermathologist")
                .calendar(Set.of())
                .build();

        Patient patient = Patient.builder()
                .pesel("11122233344")
                .name("Adam")
                .surname("Pacjentowy")
                .phone("+48 123 456 789")
                .email("adam@pacjentowy.pl")
                .build();

        String idNumber = doctor.getIdNumber();
        String pesel = patient.getPesel();
        Integer hour = 10;
        Integer badYear1 = 2025; String badMonth1 = "February"; Integer badDayOfMonth1 = 29;
        Integer badYear2 = 2024; String badMonth2 = "March"; Integer badDayOfMonth2 = 32;
        Integer badYear3 = 2024; String badMonth3 = "January"; Integer badDayOfMonth3 = 31;
        Integer goodYear = 2027; String goodMonth = "January"; Integer goodDayOfMonth = 31;

        Map<String, String> param1 = buildMap(idNumber, pesel, badYear1, badMonth1, badDayOfMonth1, hour);
        Map<String, String> param2 = buildMap(idNumber, pesel, badYear2, badMonth2, badDayOfMonth2, hour);
        Map<String, String> param3 = buildMap(idNumber, pesel, badYear3, badMonth3, badDayOfMonth3, hour);
        Map<String, String> param4 = buildMap(idNumber, pesel, goodYear, goodMonth, goodDayOfMonth, hour);

        LinkedMultiValueMap<String, String> parameters1 = new LinkedMultiValueMap<>();
        LinkedMultiValueMap<String, String> parameters2 = new LinkedMultiValueMap<>();
        LinkedMultiValueMap<String, String> parameters3 = new LinkedMultiValueMap<>();
        LinkedMultiValueMap<String, String> parameters4 = new LinkedMultiValueMap<>();
        param1.forEach(parameters1::add);
        param2.forEach(parameters2::add);
        param3.forEach(parameters3::add);
        param4.forEach(parameters4::add);

        Appointment appointment = Appointment.builder()
                .appointmentId(1)
                .idNumber("1134e1")
                .dateTime(OffsetDateTime.of(goodYear, 1, goodDayOfMonth, hour, 0, 0, 0, ZoneOffset.UTC))
                .doctor(doctor)
                .patient(patient)
                .build();
        AppointmentDTO appointmentDTO = AppointmentDTO.builder()
                .appointmentId(1)
                .idNumber("1134e1")
                .dateTime(OffsetDateTime.of(goodYear, 1, goodDayOfMonth, hour, 0, 0, 0, ZoneOffset.UTC))
                .doctor(doctor)
                .patient(patient)
                .build();
        // when, then
        Mockito.when(doctorService.findDoctor(idNumber))
                .thenReturn(doctor);
        Mockito.when(patientService.findPatient(pesel))
                .thenReturn(patient);
        Mockito.doNothing().when(appointmentService).makeAnAppointment(appointment);
        Mockito.when(appointmentMapper.map(appointment))
                .thenReturn(appointmentDTO);

        String message1 = mockMvc.perform(post(AppointmentRestController.API_APPOINTMENT + AppointmentRestController.NEW_APPOINTMENT).params(
                        parameters1))
                .andExpect(status().isInternalServerError())
                .andReturn().getResolvedException().getMessage();
        Assertions.assertEquals("There is no so many days in this month!", message1);

        String message2 = mockMvc.perform(post(AppointmentRestController.API_APPOINTMENT + AppointmentRestController.NEW_APPOINTMENT).params(
                        parameters2))
                .andExpect(status().isInternalServerError())
                .andReturn().getResolvedException().getMessage();
        Assertions.assertEquals("There is no so many days in this month!", message2);

        String message3 = mockMvc.perform(post(AppointmentRestController.API_APPOINTMENT + AppointmentRestController.NEW_APPOINTMENT).params(
                        parameters3))
                .andExpect(status().isInternalServerError())
                .andReturn().getResolvedException().getMessage();
        Assertions.assertEquals("You can't make an appointment in the past", message3);

        mockMvc.perform(post(AppointmentRestController.API_APPOINTMENT + AppointmentRestController.NEW_APPOINTMENT).params(
                        parameters4))
                .andExpect(status().isOk())
                .andReturn();

    }

    private static Map<String, String> buildMap(
            String doctorId,
            String pesel,
            Integer year,
            String month,
            Integer day,
            Integer hour
    ) {
        Map<String, String> result = new HashMap<>();
        result.put("doctorId", doctorId);
        result.put("pesel", pesel);
        result.put("year", String.valueOf(year));
        result.put("month", month);
        result.put("day", String.valueOf(day));
        result.put("hour", String.valueOf(hour));
        return result;
    }

}
