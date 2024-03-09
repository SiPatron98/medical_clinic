package org.example.business;

import lombok.AllArgsConstructor;
import org.example.business.dao.AppointmentDAO;
import org.example.business.dao.CalendarDAO;
import org.example.domain.Appointment;
import org.example.domain.Calendar;
import org.example.domain.Doctor;
import org.example.domain.exception.ProcessingException;
import org.example.infrastructure.database.repository.AppointmentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @InjectMocks
    private AppointmentService appointmentService;


    @Mock
    private AppointmentDAO appointmentDAO;

    @Mock
    private CalendarDAO calendarDAO;

    @Test
    void canFindAllAvailable() {
        // given
        List<Appointment> appointments = buildAppointmentsList();
        Mockito.when(appointmentDAO.findAvailable())
                .thenReturn(appointments);
        // when
        List<Appointment> result = appointmentService.findAvailable();

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void canFindAllAvailableByDoctorId() {
        // given
        Set<Appointment> appointments = new HashSet<>();
        appointments.addAll(buildAppointmentsList());
        String idNumber = "1111";
        Mockito.when(appointmentDAO.findAvailableByDoctorsId(idNumber))
                .thenReturn(appointments);
        // when
        Set<Appointment> result = appointmentService.findAvailableByDoctorsId(idNumber);

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void canFindAllAvailableByPatientsPesel() {
        // given
        Set<Appointment> appointments = new HashSet<>();
        appointments.add(buildAppointmentsList().get(0));
        String pesel = "1111";
        Mockito.when(appointmentDAO.findAvailableByPatientsPesel(pesel))
                .thenReturn(appointments);
        // when
        Set<Appointment> result = appointmentService.findAvailableByPatientsPesel(pesel);

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    void canFindAppointmentByItsId() {
        // given
        Appointment appointment = buildAppointmentsList().get(1);
        String idNumber = appointment.getIdNumber();
        Mockito.when(appointmentDAO.findByIdNumber(idNumber))
                .thenReturn(appointment);
        // when
        Appointment result = appointmentService.findByIdNumber(idNumber);

        // then
        Assertions.assertEquals(
                OffsetDateTime.of(2027, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC),
                result.getDateTime()
        );
    }

    @Test
    void canAppointmentBeSaved() {
        // given
        Appointment appointment = buildAppointmentsList().get(1);
        Mockito.doNothing().when(appointmentDAO).save(appointment);

        // when
        appointmentService.save(appointment);

        // then
        Mockito.verify(appointmentDAO, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void canAppointmentBeMade() {
        // given
        Appointment appointment = buildAppointmentsList().get(1).withDoctor(buildDoctor());
        Calendar calendar = Calendar.builder()
                .dateTime(appointment.getDateTime())
                .doctor(appointment.getDoctor())
                .build();
        Mockito.doNothing().when(appointmentDAO).makeAnAppointment(appointment);
        Mockito.doNothing().when(calendarDAO).addNewDate(calendar);

        // when
        appointmentService.makeAnAppointment(appointment);

        // then
        Mockito.verify(appointmentDAO, Mockito.times(1)).makeAnAppointment(Mockito.any());
        Mockito.verify(calendarDAO, Mockito.times(1)).addNewDate(Mockito.any());
    }

    @Test
    void canThrowExceptionDuringMakingAppointment() {
        // given
        Appointment badAppointment = buildAppointmentsList().get(1).withDoctor(buildDoctor()).withDateTime(
                OffsetDateTime.of(2027, 6, 28, 15, 0, 0, 0, ZoneOffset.UTC)
        );

        // when, then
        Throwable exception = Assertions.assertThrows(ProcessingException.class, () -> appointmentService.makeAnAppointment(badAppointment));
        Assertions.assertEquals("This date is not available.", exception.getMessage());
    }

    @Test
    void canAppointmentBeUpdated() {
        // given
        Appointment appointment = buildAppointmentsList().get(0).withNote("Old note");
        String newNote = "New note";
        Mockito.when(appointmentDAO.update(appointment))
                .thenReturn(appointment.withNote(newNote));

        // when
        Appointment result = appointmentService.update(appointment);

        // then
        Assertions.assertEquals("Old note", appointment.getNote());
        Assertions.assertEquals(newNote, result.getNote());
    }

    @Test
    void canAppointmentBeDeletedByItsId() {
        // given
        Appointment appointment = buildAppointmentsList().get(0);
        Mockito.doNothing().when(appointmentDAO).deleteByIdNumber(appointment.getIdNumber());

        // when
        appointmentService.deleteByIdNumber(appointment.getIdNumber());

        // then
        Mockito.verify(appointmentDAO, Mockito.times(1)).deleteByIdNumber(Mockito.any());
    }

    private List<Appointment> buildAppointmentsList() {
        Appointment appointment1 = Appointment.builder()
                .appointmentId(1)
                .idNumber("1134e1")
                .dateTime(OffsetDateTime.of(2023, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .doctor(null)
                .patient(null)
                .note("Testing note")
                .build();
        Appointment appointment2 = Appointment.builder()
                .appointmentId(2)
                .idNumber("1123d1")
                .dateTime(OffsetDateTime.of(2027, 5, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .doctor(null)
                .patient(null)
                .build();
        return List.of(appointment1, appointment2);
    }

    private Doctor buildDoctor() {
        return Doctor.builder()
                .idNumber("4653")
                .name("Adam")
                .surname("Lekarski")
                .specialization("dermathologist")
                .calendar(Set.of(buildCalendar()))
                .build();
    }

    private Calendar buildCalendar() {
        return Calendar.builder()
                .calendarId(1)
                .dateTime(OffsetDateTime.of(2027, 6, 28, 15, 0, 0, 0, ZoneOffset.UTC))
                .doctor(null)
                .build();
    }
}
