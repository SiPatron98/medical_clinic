package org.example.business;

import org.example.business.dao.DoctorDAO;
import org.example.domain.Doctor;
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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {

    @InjectMocks
    private DoctorService doctorService;

    @Mock
    private DoctorDAO doctorDAO;

    @Test
    void canFindAllDoctors() {
        // given
        List<Doctor> doctors = buildListOfDoctors();
        Mockito.when(doctorDAO.findAvailable())
                .thenReturn(doctors);

        // when
        List<Doctor> result = doctorService.findAvailable();

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void canFindDoctorByItsId() {
        // given
        Doctor doctor = buildListOfDoctors().get(0);
        String idNumber = "4653";
        Mockito.when(doctorDAO.findByIdNumber(idNumber))
                .thenReturn(Optional.of(doctor));

        // when
        Doctor result = doctorService.findDoctor(idNumber);

        // then
        Assertions.assertEquals("Adam", result.getName());
    }

    @Test
    void canThrowExceptionDuringFindingADoctor() {
        // given
        String idNumber = "0000";
        Mockito.when(doctorDAO.findByIdNumber(idNumber))
                .thenReturn(Optional.empty());

        // when, then
        Throwable ex = Assertions.assertThrows(NotFoundException.class, () -> doctorService.findDoctor(idNumber));
        Assertions.assertEquals("Could not find doctor by idNumber: [%s]".formatted(idNumber), ex.getMessage());
    }

    @Test
    void canDoctorBeSaved() {
        // given
        Doctor doctor = buildListOfDoctors().get(1);
        Mockito.doNothing().when(doctorDAO).save(doctor);

        // when
        doctorService.save(doctor);

        // then
        Mockito.verify(doctorDAO, Mockito.times(1)).save(Mockito.any());
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
}
