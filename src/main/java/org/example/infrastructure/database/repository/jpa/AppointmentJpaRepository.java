package org.example.infrastructure.database.repository.jpa;

import org.example.domain.Appointment;
import org.example.infrastructure.database.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface AppointmentJpaRepository extends JpaRepository<AppointmentEntity, Integer> {

    @Query("""
        SELECT app FROM AppointmentEntity app
        WHERE app.doctor.idNumber = :numberId
        """)
    Set<AppointmentEntity> findAvailableByDoctorsId(final @Param("numberId") String numberId);

    @Query("""
        SELECT app FROM AppointmentEntity app
        WHERE app.patient.pesel = :pesel
        """)
    Set<AppointmentEntity> findAvailableByPatientsPesel(final @Param("pesel") String pesel);

    AppointmentEntity findByIdNumber(String appointmentId);
}
