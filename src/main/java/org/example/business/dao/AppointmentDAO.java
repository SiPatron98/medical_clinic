package org.example.business.dao;

import org.example.domain.Appointment;

import java.util.List;
import java.util.Set;

public interface AppointmentDAO {
    List<Appointment> findAvailable();

    Set<Appointment> findAvailableByDoctorsId(String numberId);

    Set<Appointment> findAvailableByPatientsPesel(String pesel);

    Appointment findByIdNumber(String appointmentId);

    void save(Appointment appointment);

    void makeAnAppointment(Appointment appointment);

    void update(Appointment appointment);
}
