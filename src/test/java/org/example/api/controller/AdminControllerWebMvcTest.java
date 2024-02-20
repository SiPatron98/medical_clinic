package org.example.api.controller;

import lombok.AllArgsConstructor;
import org.example.api.dto.DoctorDTO;
import org.example.api.dto.PatientDTO;
import org.example.api.dto.mapper.DoctorMapper;
import org.example.api.dto.mapper.PatientMapper;
import org.example.business.DoctorService;
import org.example.business.PatientService;
import org.example.domain.Doctor;
import org.example.domain.Patient;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AdminControllerWebMvcTest {

    private MockMvc mockMvc;

    @MockBean
    private final DoctorService doctorService;
    @MockBean
    private final DoctorMapper doctorMapper;
    @MockBean
    private final PatientService patientService;
    @MockBean
    private final PatientMapper patientMapper;

    @Test
    void isAdminPanelWorksCorrectly() throws Exception {
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

        // when, then
        Mockito.when(patientService.findAvailable())
                .thenReturn(List.of(patient));
        Mockito.when(patientMapper.map(patient))
                .thenReturn(patientDTO);
        Mockito.when(doctorService.findAvailable())
                .thenReturn(List.of(doctor));
        Mockito.when(doctorMapper.map(doctor))
                .thenReturn(doctorDTO);

        mockMvc.perform(get(AdminController.ADMIN))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("availableDoctorDTOs"))
                .andExpect(model().attributeExists("availablePatientDTOs"))
                .andExpect(view().name("admin"));
    }

    @Test
    void isAddingNewDoctorWorksCorrectly() throws Exception {
        // given
        DoctorDTO doctorDTO = DoctorDTO.buildDefaultDoctor();
        Doctor doctor = Doctor.builder()
                .idNumber("4653")
                .name("Adam")
                .surname("Lekarski")
                .specialization("dermathologist")
                .calendar(Set.of())
                .build();

        // when, then
        Mockito.when(doctorMapper.map(doctor))
                .thenReturn(doctorDTO);
        Mockito.doNothing().when(doctorService).save(doctor);

        mockMvc.perform(post(AdminController.ADMIN + "/add_doctor")
                    .param("idNumber", doctor.getIdNumber())
                    .param("name", doctor.getName())
                    .param("surname", doctor.getSurname())
                    .param("specialization", doctor.getSpecialization()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/admin"));
    }

    @Test
    void isAddingNewPatientWorksCorrectly() throws Exception {
        // given
        PatientDTO patientDTO = PatientDTO.buildDefaultPatient();
        Patient patient = Patient.builder()
                .pesel("11122233344")
                .name("Adam")
                .surname("Pacjentowy")
                .phone("+48 123 456 789")
                .email("adam@pacjentowy.pl")
                .build();

        // when, then
        Mockito.when(patientMapper.map(patientDTO))
                .thenReturn(patient);
        Mockito.doNothing().when(patientService).save(patient);

        mockMvc.perform(post(AdminController.ADMIN + "/add_patient").flashAttr("patientDTO", patientDTO)
                        .param("pesel", patient.getPesel())
                        .param("name", patient.getName())
                        .param("surname", patient.getSurname())
                        .param("phone", patient.getPhone())
                        .param("email", patient.getEmail()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/admin"));
    }

    @Test
    void thatEmailValidationWorksCorrectly() throws Exception {
        // given
        String badEmail = "badEmail";
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = PatientDTO.buildDefaultPatient().withEmail(badEmail).asMap();
        parametersMap.forEach(parameters::add);

        // when, then
        mockMvc.perform(post(AdminController.ADMIN + "/add_patient").params(parameters))
                .andExpect(status().isBadRequest())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", Matchers.containsString(badEmail)))
                .andExpect(view().name("error"));
    }
}
