package org.example.api.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.example.api.dto.AppointmentDTO;
import org.example.api.dto.DoctorDTO;
import org.example.api.dto.PatientDTO;
import org.example.api.dto.mapper.DoctorMapper;
import org.example.api.dto.mapper.PatientMapper;
import org.example.business.DoctorService;
import org.example.business.PatientService;
import org.example.domain.Doctor;
import org.example.domain.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AdminRestControllerWebMvcTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @MockBean
    private final DoctorService doctorService;
    @MockBean
    private final DoctorMapper doctorMapper;
    @MockBean
    private final PatientService patientService;
    @MockBean
    private final PatientMapper patientMapper;

    @Test
    void isFindingAllDoctorsWorksCorrectly() throws Exception {
        // given
        List<DoctorDTO> doctorDTOList = buildDoctorsDTOList();
        Doctor doctor1 = Doctor.builder()
                .idNumber("4653")
                .name("Adam")
                .surname("Lekarski")
                .specialization("dermathologist")
                .calendar(Set.of())
                .build();
        Doctor doctor2 = Doctor.builder()
                .idNumber("4654")
                .name("Marcin")
                .surname("Doktorski")
                .specialization("urologhist")
                .calendar(Set.of())
                .build();
        List<Doctor> doctorList = List.of(doctor1, doctor2);

        // when, then
        Mockito.when(doctorService.findAvailable())
                .thenReturn(doctorList);


        for (int i = 0; i < doctorDTOList.size(); i++) {
            Mockito.when(doctorMapper.map(doctorList.get(i)))
                    .thenReturn(doctorDTOList.get(i));
        }

        mockMvc.perform(get(AdminRestController.API_ADMIN + AdminRestController.ALL_DOCTORS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idNumber").value(doctorDTOList.get(0).getIdNumber()))
                .andExpect(jsonPath("$[1].idNumber").value(doctorDTOList.get(1).getIdNumber()))
                .andReturn();

    }

    @Test
    void isFindingAllPatientsWorksCorrectly() throws Exception {
        // given
        List<PatientDTO> patientDTOList = buildPatientsDTOList();
        Patient patient1 = Patient.builder()
                .pesel("11122233344")
                .name("Adam")
                .surname("Pacjentowy")
                .phone("+48 123 456 789")
                .email("adam@pacjentowy.pl")
                .build();
        Patient patient2 = Patient.builder()
                .pesel("55566677788")
                .name("Marcin")
                .surname("Pacjenciak")
                .phone("+48 987 654 321")
                .email("marcin@pacjenciak.pl")
                .build();
        List<Patient> patientList = List.of(patient1, patient2);

        // when, then
        Mockito.when(patientService.findAvailable())
                .thenReturn(patientList);


        for (int i = 0; i < patientDTOList.size(); i++) {
            Mockito.when(patientMapper.map(patientList.get(i)))
                    .thenReturn(patientDTOList.get(i));
        }

        mockMvc.perform(get(AdminRestController.API_ADMIN + AdminRestController.ALL_PATIENTS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pesel").value(patientDTOList.get(0).getPesel()))
                .andExpect(jsonPath("$[1].pesel").value(patientDTOList.get(1).getPesel()))
                .andReturn();

    }

    @Test
    void isAddingADoctorWorksCorrectly() throws Exception {
        // given
        DoctorDTO doctorDTO = buildDoctorsDTOList().get(0);
        Doctor doctor = Doctor.builder()
                .idNumber("4653")
                .name("Adam")
                .surname("Lekarski")
                .specialization("dermathologist")
                .calendar(Set.of())
                .build();
        Map<String, String> param = buildMap(
                doctor.getIdNumber(),
                doctor.getName(),
                doctor.getSurname(),
                doctor.getSpecialization()
        );
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        param.forEach(parameters::add);

        // when, then
        Mockito.when(doctorMapper.map(doctorDTO))
                .thenReturn(doctor);
        Mockito.doNothing().when(doctorService).save(doctor);
        mockMvc.perform(post(AdminRestController.API_ADMIN + AdminRestController.NEW_DOCTOR).params(parameters))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idNumber").value(doctor.getIdNumber()))
                .andReturn();
    }

    @Test
    void isAddingAPatientWorksCorrectly() throws Exception {
        // given
        PatientDTO patientDTOBody = PatientDTO.buildDefaultPatient();
        Patient patient = Patient.builder()
                .pesel("11122233344")
                .name("Adam")
                .surname("Pacjentowy")
                .phone("+48 123 456 789")
                .email("adam@pacjentowy.pl")
                .build();

        String requestBody = objectMapper.writeValueAsString(patientDTOBody);

        // when, then
        when(patientMapper.map(patientDTOBody)).thenReturn(patient);
        doNothing().when(patientService).save(patient);
        String responseBody = objectMapper.writeValueAsString(patientDTOBody);

        MvcResult result = mockMvc.perform(post(AdminRestController.API_ADMIN + AdminRestController.NEW_PATIENT)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pesel", is(patientDTOBody.getPesel())))
                .andExpect(jsonPath("$.phone", is(patientDTOBody.getPhone())))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isEqualTo(responseBody);
    }

    @ParameterizedTest
    @MethodSource
    void thatPhoneValidationWorksCorrectly(Boolean correctPhone, String phone) throws Exception {
        // given
        PatientDTO patientDTOBody = PatientDTO.buildDefaultPatient().withPhone(phone);
        String requestBody = objectMapper.writeValueAsString(patientDTOBody);
        Patient patient = Patient.builder()
                .pesel("11122233344")
                .name("Adam")
                .surname("Pacjentowy")
                .phone("+48 123 456 789")
                .email("adam@pacjentowy.pl")
                .build().withPhone(phone);

        // when, then
        if (correctPhone) {
            String responseBody = objectMapper.writeValueAsString(patientDTOBody);
            when(patientMapper.map(patientDTOBody)).thenReturn(patient);
            doNothing().when(patientService).save(patient);

            MvcResult result = mockMvc.perform(post(AdminRestController.API_ADMIN + AdminRestController.NEW_PATIENT)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.pesel", is(patientDTOBody.getPesel())))
                    .andExpect(jsonPath("$.phone", is(patientDTOBody.getPhone())))
                    .andReturn();

            assertThat(result.getResponse().getContentAsString()).isEqualTo(responseBody);
        } else {
            mockMvc.perform(post(AdminRestController.API_ADMIN + AdminRestController.NEW_PATIENT)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorId", notNullValue()));
        }
    }

    public static Stream<Arguments> thatPhoneValidationWorksCorrectly() {
        return Stream.of(
                Arguments.of(false, ""),
                Arguments.of(false, "+48 504 203 260@@"),
                Arguments.of(false, "+48.504.203.260"),
                Arguments.of(false, "+55(123) 456-78-90-"),
                Arguments.of(false, "+55(123) - 456-78-90"),
                Arguments.of(false, "504.203.260"),
                Arguments.of(false, " "),
                Arguments.of(false, "-"),
                Arguments.of(false, "()"),
                Arguments.of(false, "() + ()"),
                Arguments.of(false, "(21 7777"),
                Arguments.of(false, "+48 (21)"),
                Arguments.of(false, "+"),
                Arguments.of(false, " 1"),
                Arguments.of(false, "1"),
                Arguments.of(false, "+48 (12) 504 203 260"),
                Arguments.of(false, "+48 (12) 504-203-260"),
                Arguments.of(false, "+48(12)504203260"),
                Arguments.of(false, "555-5555-555"),
                Arguments.of(true, "+48 504 203 260")
        );
    }

    private List<DoctorDTO> buildDoctorsDTOList() {
        DoctorDTO doctorDTO1 =  DoctorDTO.builder()
                .idNumber("4653")
                .name("Adam")
                .surname("Lekarski")
                .specialization("dermathologist")
                .calendar(Set.of())
                .build();
        DoctorDTO doctorDTO2 =  DoctorDTO.builder()
                .idNumber("4654")
                .name("Marcin")
                .surname("Doktorski")
                .specialization("urologhist")
                .calendar(Set.of())
                .build();
        return List.of(doctorDTO1, doctorDTO2);
    }

    private List<PatientDTO> buildPatientsDTOList() {
        PatientDTO patientDTO1 =  PatientDTO.builder()
                .pesel("11122233344")
                .name("Adam")
                .surname("Pacjentowy")
                .phone("+48 123 456 789")
                .email("adam@pacjentowy.pl")
                .build();
        PatientDTO patientDTO2 =  PatientDTO.builder()
                .pesel("55566677788")
                .name("Marcin")
                .surname("Pacjenciak")
                .phone("+48 987 654 321")
                .email("marcin@pacjenciak.pl")
                .build();
        return List.of(patientDTO1, patientDTO2);
    }

    private Map<String, String> buildMap(
            String idNumber,
            String name,
            String surname,
            String specialization
    ) {
        Map<String, String> result = new HashMap<>();
        result.put("idNumber", idNumber);
        result.put("name", name);
        result.put("surname", surname);
        result.put("specialization", specialization);
        return result;
    }
}
