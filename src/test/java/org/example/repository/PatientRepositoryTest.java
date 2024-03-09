package org.example.repository;

import org.example.domain.Patient;
import org.example.infrastructure.database.entity.PatientEntity;
import org.example.infrastructure.database.repository.PatientRepository;
import org.example.infrastructure.database.repository.jpa.PatientJpaRepository;
import org.example.infrastructure.database.repository.mapper.PatientEntityMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PatientRepositoryTest {
    @InjectMocks
    private PatientRepository patientRepository;

    @Mock
    private PatientJpaRepository patientJpaRepository;
    @Mock
    private PatientEntityMapper patientEntityMapper;

    @Test
    void canFindAvailablePatients() {
        // given
        List<Patient> patients = buildListOfPatients();
        List<PatientEntity> patientEntities = buildListOfPatientEntities();
        Mockito.when(patientJpaRepository.findAll())
                .thenReturn(patientEntities);

        for (int i = 0; i < patients.size(); i++) {
            Mockito.when(patientEntityMapper.mapFromEntity(patientEntities.get(i)))
                    .thenReturn(patients.get(i));
        }

        // when
        List<Patient> result = patientRepository.findAvailable();

        // then
        Assertions.assertEquals("Adam", result.get(0).getName());
        Assertions.assertEquals("Marcin", result.get(1).getName());
    }

    @Test
    void canFindPatientByItsPesel() {
        // given
        String pesel = "11122233344";
        Patient patient = buildListOfPatients().get(0);
        PatientEntity patientEntity = buildListOfPatientEntities().get(0);
        Mockito.when(patientJpaRepository.findByPesel(pesel))
                .thenReturn(Optional.of(patientEntity));
        Mockito.when(patientEntityMapper.mapFromEntityWithAddressAndDiseases(patientEntity))
                .thenReturn(patient);


        // when
        Patient result = patientRepository.findByPesel(pesel).get();

        // then
        Assertions.assertEquals("Adam", result.getName());
    }

    @Test
    void canSaveAPatient() {
        // given
        Patient patient = buildListOfPatients().get(1);
        PatientEntity patientEntity = buildListOfPatientEntities().get(1);

        Mockito.when(patientEntityMapper.mapToEntity(patient))
                .thenReturn(patientEntity);

        Mockito.when(patientJpaRepository.saveAndFlush(patientEntity))
                .thenReturn(patientEntity);


        // when
        patientRepository.save(patient);

        // then
        Mockito.verify(patientJpaRepository, Mockito.times(1)).saveAndFlush(Mockito.any());
        Mockito.verify(patientEntityMapper, Mockito.times(1)).mapToEntity(Mockito.any());
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

    private List<PatientEntity> buildListOfPatientEntities() {
        PatientEntity patient = PatientEntity.builder()
                .pesel("11122233344")
                .name("Adam")
                .surname("Pacjentowy")
                .phone("+48 123 456 789")
                .email("adam@pacjentowy.pl")
                .build();
        PatientEntity patient2 = PatientEntity.builder()
                .pesel("55566677788")
                .name("Marcin")
                .surname("Pacjenciak")
                .phone("+48 987 654 321")
                .email("marcin@pacjenciak.pl")
                .build();
        return List.of(patient, patient2);
    }
}
