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
import org.springframework.util.LinkedMultiValueMap;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(controllers = DoctorController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DoctorControllerWebMvcTest {

    private MockMvc mockMvc;

    @MockBean
    private final DoctorService doctorService;
    @MockBean
    private final DoctorMapper doctorMapper;
    @MockBean
    private final AppointmentService appointmentService;
    @MockBean
    private final AppointmentMapper appointmentMapper;
    @MockBean
    private final DiseaseMapper diseaseMapper;


    @Test
    void isDoctorPanelWorksCorrectly() throws Exception {
        // given
        LinkedMultiValueMap<String, String> paremeters = new LinkedMultiValueMap<>();
        Map<String, String> paramMap = DoctorDTO.buildDefaultDoctor().asMap();

        DoctorDTO doctorDTO = DoctorDTO.buildDefaultDoctor();
        Doctor doctor = Doctor.builder()
                .idNumber("4653")
                .name("Adam")
                .surname("Lekarski")
                .specialization("dermathologist")
                .calendar(Set.of())
                .build();
        List<AppointmentDTO> appointmentDTOS = buildAppointmentsDTOList();
        Set<Appointment> appointments = buildAppointmentsSet();
        paramMap.forEach(paremeters::add);
        int i = 0;

        // when, then
        Mockito.when(doctorService.findAvailable())
                .thenReturn(List.of(doctor));
        Mockito.when(doctorMapper.map(doctor))
                .thenReturn(doctorDTO);
        Mockito.when(appointmentService.findAvailableByDoctorsId(doctor.getIdNumber()))
                .thenReturn(appointments);
        Mockito.when(doctorService.findDoctor(doctorDTO.getIdNumber()))
                .thenReturn(doctor);

        for (Appointment appoint : appointments) {
            Mockito.when(appointmentMapper.map(appoint))
                    .thenReturn(appointmentDTOS.get(i));
            i++;
        }


        mockMvc.perform(get(DoctorController.DOCTOR).params(paremeters))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("allIdNumbers"))
                .andExpect(model().attributeExists("allDoctorAppointments"))
                .andExpect(model().attributeExists("idDoctorNumber"))
                .andExpect(model().attributeExists("futureDoctorAppointments"))
                .andExpect(model().attributeExists("pastDoctorAppointments"))
                .andExpect(model().attributeExists("doctor"))
                .andExpect(view().name("doctor"));
    }

    @Test
    void isShowAppointmentDetailsWorksCorrectly() throws Exception {
        // given
        AppointmentDTO appointmentDTO = buildAppointmentsDTOList().get(0);
        Appointment appointment = Appointment.builder()
                .appointmentId(1)
                .idNumber("1134e1")
                .dateTime(OffsetDateTime.of(2023, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .doctor(null)
                .patient(buildPatient())
                .note("Testing note")
                .build();

        // when, then

        Mockito.when(appointmentService.findByIdNumber(appointmentDTO.getIdNumber()))
                .thenReturn(appointment);
        Mockito.when(appointmentMapper.map(appointment))
                .thenReturn(appointmentDTO);
        Mockito.when(diseaseMapper.map(buildDisease()))
                        .thenReturn(DiseaseDTO.builder()
                                .name(buildDisease().getName())
                                .build());

        mockMvc.perform(get(DoctorController.DOCTOR + "/details/{appointmentId}", appointmentDTO.getIdNumber()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("note"))
                .andExpect(model().attributeExists("patientDiseases"))
                .andExpect(model().attributeExists("appointmentDTO"))
                .andExpect(view().name("appointment_details"));
    }

    @Test
    void isShowEditAppointmentNoteWorksCorrectly() throws Exception {
        // given
        AppointmentDTO appointmentDTO = buildAppointmentsDTOList().get(0);
        UpdateAppointmentDTO updateAppointmentDTO = new UpdateAppointmentDTO();
        updateAppointmentDTO = updateAppointmentDTO.withIdNumber(appointmentDTO.getIdNumber());

        // when, then

        mockMvc.perform(get(DoctorController.DOCTOR + "/edit/{appointmentId}", appointmentDTO.getIdNumber()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("updateAppointmentDTO"))
                .andExpect(model().attributeExists("appointmentId"))
                .andExpect(view().name("appointment_edit"));
    }

    @Test
    void isEditingAppointmentNoteWorksCorrectly() throws Exception {
        // given
        AppointmentDTO appointmentDTO = buildAppointmentsDTOList().get(0);
        UpdateAppointmentDTO updateAppointmentDTO = new UpdateAppointmentDTO();
        updateAppointmentDTO = updateAppointmentDTO.withIdNumber(appointmentDTO.getIdNumber());
        Appointment appointment = Appointment.builder()
                .appointmentId(1)
                .idNumber("1134e1")
                .dateTime(OffsetDateTime.of(2023, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .doctor(null)
                .patient(buildPatient())
                .note("Testing note")
                .build();
        String newNote = "New note";
        updateAppointmentDTO = updateAppointmentDTO.withNote(newNote);

        // when, then
        Mockito.when(appointmentService.findByIdNumber(updateAppointmentDTO.getIdNumber()))
                        .thenReturn(appointment);
        Mockito.when(appointmentMapper.map(appointment))
                .thenReturn(appointmentDTO);
        appointmentDTO = appointmentDTO.withNote(updateAppointmentDTO.getNote());
        appointment = appointment.withNote(updateAppointmentDTO.getNote());
        Mockito.when(appointmentMapper.map(appointmentDTO))
                        .thenReturn(appointment);
        Mockito.when(appointmentService.update(appointment))
                        .thenReturn(appointment);


        mockMvc.perform(put(DoctorController.DOCTOR + "/edit/{appointmentId}", appointmentDTO.getIdNumber())
                        .flashAttr("updateAppointmentDTO", updateAppointmentDTO))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/doctor"));
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
}
