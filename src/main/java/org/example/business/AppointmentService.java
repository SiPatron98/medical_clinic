package org.example.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.business.dao.AppointmentDAO;
import org.example.business.dao.CalendarDAO;
import org.example.domain.Appointment;
import org.example.domain.Calendar;
import org.example.domain.exception.ProcessingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class AppointmentService {

    private final AppointmentDAO appointmentDAO;
    private final CalendarDAO calendarDAO;

    @Transactional
    public List<Appointment> findAvailable() {
        List<Appointment> availableAppointments = appointmentDAO.findAvailable();
        log.info("Available appointments: [{}]", availableAppointments.size());
        return availableAppointments;
    }

    @Transactional
    public Set<Appointment> findAvailableByDoctorsId(String numberId) {
        Set<Appointment> appointments = appointmentDAO.findAvailableByDoctorsId(numberId);
        return appointments;
    }

    @Transactional
    public Set<Appointment> findAvailableByPatientsPesel(String pesel) {
        Set<Appointment> appointments = appointmentDAO.findAvailableByPatientsPesel(pesel);
        return appointments;
    }

    @Transactional
    public Appointment findByIdNumber(String appointmentId) {
        return appointmentDAO.findByIdNumber(appointmentId);
    }

    @Transactional
    public void save(Appointment appointment) {
        appointmentDAO.save(appointment);
    }

    @Transactional
    public void makeAnAppointment(Appointment appointment) {
        OffsetDateTime appointmentDateTime = appointment.getDateTime();
        Set<Calendar> calendar = appointment.getDoctor().getCalendar();

        for (Calendar date : calendar) {
            if (appointmentDateTime.equals(date.getDateTime())) {
                throw new ProcessingException("This date is not available.");
            }
        }
        appointmentDAO.makeAnAppointment(appointment);
        calendarDAO.addNewDate(Calendar.builder()
                .dateTime(appointmentDateTime)
                .doctor(appointment.getDoctor())
                .build());
    }

    @Transactional
    public void update(Appointment appointment) {
        appointmentDAO.update(appointment);
    }
}
