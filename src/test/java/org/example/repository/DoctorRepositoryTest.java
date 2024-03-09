package org.example.repository;

import org.example.domain.Doctor;
import org.example.infrastructure.database.entity.DoctorEntity;
import org.example.infrastructure.database.repository.DoctorRepository;
import org.example.infrastructure.database.repository.jpa.DoctorJpaRepository;
import org.example.infrastructure.database.repository.mapper.DoctorEntityMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class DoctorRepositoryTest {

    @InjectMocks
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorJpaRepository doctorJpaRepository;
    @Mock
    private DoctorEntityMapper doctorEntityMapper;

    @Test
    void canFindAvailableDoctors() {
        // given
        List<Doctor> doctors = buildListOfDoctors();
        List<DoctorEntity> doctorEntities = buildListOfDoctorEntities();
        Mockito.when(doctorJpaRepository.findAll())
                .thenReturn(doctorEntities);

        for (int i = 0; i < doctors.size(); i++) {
            Mockito.when(doctorEntityMapper.mapFromEntity(doctorEntities.get(i)))
                    .thenReturn(doctors.get(i));
        }

        // when
        List<Doctor> result = doctorRepository.findAvailable();

        // then
        Assertions.assertEquals("Adam", result.get(0).getName());
        Assertions.assertEquals("Marcin", result.get(1).getName());
    }

    @Test
    void canFindDoctorByItsId() {
        // given
        String idNumber = "4653";
        Doctor doctor = buildListOfDoctors().get(0);
        DoctorEntity doctorEntity = buildListOfDoctorEntities().get(0);
        Mockito.when(doctorJpaRepository.findByIdNumber(idNumber))
                .thenReturn(Optional.of(doctorEntity));
        Mockito.when(doctorEntityMapper.mapFromEntity(doctorEntity))
                .thenReturn(doctor);


        // when
        Doctor result = doctorRepository.findByIdNumber(idNumber).get();

        // then
        Assertions.assertEquals("Adam", result.getName());
    }

    @Test
    void canSaveADoctor() {
        // given
        Doctor doctor = buildListOfDoctors().get(1);
        DoctorEntity doctorEntity = buildListOfDoctorEntities().get(1);

        Mockito.when(doctorEntityMapper.mapToEntity(doctor))
                .thenReturn(doctorEntity);

        Mockito.when(doctorJpaRepository.saveAndFlush(doctorEntity))
                .thenReturn(doctorEntity);


        // when
        doctorRepository.save(doctor);

        // then
        Mockito.verify(doctorJpaRepository, Mockito.times(1)).saveAndFlush(Mockito.any());
        Mockito.verify(doctorEntityMapper, Mockito.times(1)).mapToEntity(Mockito.any());
    }


    private List<Doctor> buildListOfDoctors() {
        Doctor doctor = Doctor.builder()
                .idNumber("4653")
                .name("Adam")
                .surname("Lekarski")
                .specialization("dermatologist")
                .calendar(Set.of())
                .build();
        Doctor doctor2 = Doctor.builder()
                .idNumber("3675")
                .name("Marcin")
                .surname("Doktorski")
                .specialization("eye doctor")
                .calendar(Set.of())
                .build();
        return List.of(doctor, doctor2);
    }

    private List<DoctorEntity> buildListOfDoctorEntities() {
        DoctorEntity doctor = DoctorEntity.builder()
                .idNumber("4653")
                .name("Adam")
                .surname("Lekarski")
                .specialization("dermatologist")
                .calendar(Set.of())
                .build();
        DoctorEntity doctor2 = DoctorEntity.builder()
                .idNumber("3675")
                .name("Marcin")
                .surname("Doktorski")
                .specialization("eye doctor")
                .calendar(Set.of())
                .build();
        return List.of(doctor, doctor2);
    }
}
