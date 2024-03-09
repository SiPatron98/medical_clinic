package org.example.business;

import org.example.business.dao.PatientDAO;
import org.example.domain.Patient;
import org.example.domain.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @InjectMocks
    private PatientService patientService;

    @Mock
    private PatientDAO patientDAO;

    @Test
    void canFindAllDoctors() {
        // given
        List<Patient> patients = buildListOfPatients();
        Mockito.when(patientDAO.findAvailable())
                .thenReturn(patients);

        // when
        List<Patient> result = patientService.findAvailable();

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void canFindDoctorByItsPesel() {
        // given
        Patient patient = buildListOfPatients().get(0);
        String pesel = "11122233344";
        Mockito.when(patientDAO.findByPesel(pesel))
                .thenReturn(Optional.of(patient));

        // when
        Patient result = patientService.findPatient(pesel);

        // then
        Assertions.assertEquals("Adam", result.getName());
        Assertions.assertEquals("Pacjentowy", result.getSurname());
    }

    @Test
    void canThrowExceptionDuringFindingAPatient() {
        // given
        String pesel = "00000000000";
        Mockito.when(patientDAO.findByPesel(pesel))
                .thenReturn(Optional.empty());

        // when, then
        Throwable ex = Assertions.assertThrows(NotFoundException.class, () -> patientService.findPatient(pesel));
        Assertions.assertEquals("Could not find patient by pesel: [%s]".formatted(pesel), ex.getMessage());
    }

    @Test
    void canPatientBeSaved() {
        // given
        Patient patient = buildListOfPatients().get(1);
        Mockito.doNothing().when(patientDAO).save(patient);

        // when
        patientService.save(patient);

        // then
        Mockito.verify(patientDAO, Mockito.times(1)).save(Mockito.any());
    }

    private List<Patient> buildListOfPatients() {
        Patient patient = Patient.builder()
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
        return List.of(patient, patient2);
    }
}
