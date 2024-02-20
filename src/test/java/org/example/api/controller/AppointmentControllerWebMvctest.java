package org.example.api.controller;

import lombok.AllArgsConstructor;
import org.example.api.dto.AppointmentDTO;
import org.example.api.dto.DoctorDTO;
import org.example.api.dto.PatientDTO;
import org.example.api.dto.mapper.AppointmentMapper;
import org.example.api.dto.mapper.DoctorMapper;
import org.example.api.dto.mapper.PatientMapper;
import org.example.business.AppointmentService;
import org.example.business.DoctorService;
import org.example.business.PatientService;
import org.example.domain.Appointment;
import org.example.domain.Disease;
import org.example.domain.Doctor;
import org.example.domain.Patient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AppointmentController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AppointmentControllerWebMvctest {

    private MockMvc mockMvc;

    @MockBean
    private final PatientService patientService;
    @MockBean
    private final PatientMapper patientMapper;
    @MockBean
    private final DoctorService doctorService;
    @MockBean
    private final DoctorMapper doctorMapper;
    @MockBean
    private final AppointmentService appointmentService;
    @MockBean
    private final AppointmentMapper appointmentMapper;

    @Test
    void isAppointmentPanelWorksCorrectly() throws Exception {
        // given
        DoctorDTO doctorDTO = DoctorDTO.buildDefaultDoctor();
        PatientDTO patientDTO = PatientDTO.buildDefaultPatient();
        Patient patient = Patient.builder()
                .pesel("11122233344")
                .name("Adam")
                .surname("Pacjentowy")
                .phone("+48 123 456 789")
                .email("adam@pacjentowy.pl")
                .build();
        Doctor doctor = Doctor.builder()
                .idNumber("4653")
                .name("Adam")
                .surname("Lekarski")
                .specialization("dermathologist")
                .calendar(Set.of())
                .build();
        Set<Appointment> appointments = buildAppointmentsSet();
        List<AppointmentDTO> appointmentDTOS = buildAppointmentsDTOList();
        int i = 0;

        // when, then
        Mockito.when(patientService.findAvailable())
                        .thenReturn(List.of(patient));
        Mockito.when(patientMapper.map(patient))
                        .thenReturn(patientDTO);
        Mockito.when(doctorService.findAvailable())
                .thenReturn(List.of(doctor));
        Mockito.when(doctorMapper.map(doctor))
                .thenReturn(doctorDTO);
        Mockito.when(appointmentService.findAvailableByDoctorsId(doctor.getIdNumber()))
                .thenReturn(buildAppointmentsSet());
        Mockito.when(appointmentService.findAvailableByPatientsPesel(patient.getPesel()))
                        .thenReturn(buildAppointmentsSet());

        for (Appointment appoint : appointments) {
            Mockito.when(appointmentMapper.map(appoint))
                    .thenReturn(appointmentDTOS.get(i));
            i++;
        }


        mockMvc.perform(get("/new_appointment")
                        .param("doctorId", doctor.getIdNumber())
                        .param("pesel", patient.getPesel()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("allPesels"))
                .andExpect(model().attributeExists("doctorIds"))
                .andExpect(model().attributeExists("doctorId"))
                .andExpect(model().attributeExists("pesel"))
                .andExpect(model().attributeExists("futureDoctorAppointments"))
                .andExpect(model().attributeExists("futurePatientAppointments"))
                .andExpect(model().attributeExists("futureAppointments"))
                .andExpect(model().attributeExists("years"))
                .andExpect(model().attributeExists("days"))
                .andExpect(model().attributeExists("hours"))
                .andExpect(model().attributeExists("mapOfDaysAndMonths"))
                .andExpect(model().attributeExists("mapOfDaysAndMonthsLeapYear"))
                .andExpect(view().name("new_appointment"));
    }

    @Test
    void isMakingAppointmentWorksCorrectly() throws Exception {
        // given
        Patient patient = Patient.builder()
                .pesel("11122233344")
                .name("Adam")
                .surname("Pacjentowy")
                .phone("+48 123 456 789")
                .email("adam@pacjentowy.pl")
                .build();
        Doctor doctor = Doctor.builder()
                .idNumber("4653")
                .name("Adam")
                .surname("Lekarski")
                .specialization("dermathologist")
                .calendar(Set.of())
                .build();
        Integer hour = 10;
        Integer badYear1 = 2025; String badMonth1 = "February"; Integer badDayOfMonth1 = 29;
        Integer badYear2 = 2024; String badMonth2 = "March"; Integer badDayOfMonth2 = 32;
        Integer badYear3 = 2024; String badMonth3 = "January"; Integer badDayOfMonth3 = 31;
        Integer goodYear = 2027; String goodMonth = "January"; Integer goodDayOfMonth = 31;
        Appointment appointment = Appointment.builder()
                .dateTime(OffsetDateTime.of(goodYear, 1, goodDayOfMonth, hour, 0, 0, 0, ZoneOffset.UTC))
                .doctor(doctor)
                .patient(patient)
                .build();

        // when, then
        Mockito.when(patientService.findPatient(patient.getPesel()))
                .thenReturn(patient);
        Mockito.when(doctorService.findDoctor(doctor.getIdNumber()))
                .thenReturn(doctor);
        Mockito.doNothing().when(appointmentService).makeAnAppointment(appointment);

        mockMvc.perform(post("/new_appointment/new")
                        .param("doctorId", doctor.getIdNumber())
                        .param("pesel", patient.getPesel())
                        .param("year", String.valueOf(badYear1))
                        .param("month", badMonth1)
                        .param("day", String.valueOf(badDayOfMonth1))
                        .param("hour", String.valueOf(hour)))
                .andExpect(status().isInternalServerError())
                .andExpect(model().attribute("errorMessage", "Processing exception occurred: [There is no so many days in this month!]"))
                .andExpect(view().name("error"));

        mockMvc.perform(post("/new_appointment/new")
                        .param("doctorId", doctor.getIdNumber())
                        .param("pesel", patient.getPesel())
                        .param("year", String.valueOf(badYear2))
                        .param("month", badMonth2)
                        .param("day", String.valueOf(badDayOfMonth2))
                        .param("hour", String.valueOf(hour)))
                .andExpect(status().isInternalServerError())
                .andExpect(model().attribute("errorMessage", "Processing exception occurred: [There is no so many days in this month!]"))
                .andExpect(view().name("error"));

        mockMvc.perform(post("/new_appointment/new")
                        .param("doctorId", doctor.getIdNumber())
                        .param("pesel", patient.getPesel())
                        .param("year", String.valueOf(badYear3))
                        .param("month", badMonth3)
                        .param("day", String.valueOf(badDayOfMonth3))
                        .param("hour", String.valueOf(hour)))
                .andExpect(status().isInternalServerError())
                .andExpect(model().attribute("errorMessage", "Processing exception occurred: [You can't make an appointment in the past]"))
                .andExpect(view().name("error"));


        mockMvc.perform(post("/new_appointment/new")
                        .param("doctorId", doctor.getIdNumber())
                        .param("pesel", patient.getPesel())
                        .param("year", String.valueOf(goodYear))
                        .param("month", goodMonth)
                        .param("day", String.valueOf(goodDayOfMonth))
                        .param("hour", String.valueOf(hour)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("patientName"))
                .andExpect(model().attributeExists("patientSurname"))
                .andExpect(model().attributeExists("doctorName"))
                .andExpect(model().attributeExists("doctorSurname"))
                .andExpect(model().attributeExists("appointmentDate"))
                .andExpect(view().name("success_appointment"));
    }

    private List<AppointmentDTO> buildAppointmentsDTOList() {
        AppointmentDTO appointmentDTO1 = AppointmentDTO.builder()
                .appointmentId(1)
                .idNumber("1134e1")
                .dateTime(OffsetDateTime.of(2023, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .doctor(null)
                .patient(buildPatient())
                .note("Testing note")
                .build();
        AppointmentDTO appointmentDTO2 = AppointmentDTO.builder()
                .appointmentId(2)
                .idNumber("1123d1")
                .dateTime(OffsetDateTime.of(2027, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .doctor(null)
                .patient(buildPatient())
                .build();
        return List.of(appointmentDTO1, appointmentDTO2);
    }

    private Set<Appointment> buildAppointmentsSet() {
        Appointment appointment1 = Appointment.builder()
                .appointmentId(1)
                .idNumber("1134e1")
                .dateTime(OffsetDateTime.of(2023, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .doctor(null)
                .patient(buildPatient())
                .build();
        Appointment appointment2 = Appointment.builder()
                .appointmentId(2)
                .idNumber("1123d1")
                .dateTime(OffsetDateTime.of(2027, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .doctor(null)
                .patient(buildPatient())
                .build();
        return Set.of(appointment1, appointment2);
    }

    private Patient buildPatient() {
        return Patient.builder()
                .pesel("11122233344")
                .name("Adam")
                .surname("Pacjentowy")
                .phone("+48 123 456 789")
                .email("adam@pacjentowy.pl")
                .diseases(Set.of(buildDisease()))
                .build();
    }

    private Disease buildDisease() {
        return Disease.builder()
                .diseaseId(1)
                .name("Cukrzyca")
                .build();
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
