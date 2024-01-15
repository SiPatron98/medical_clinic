package org.example.business.dao;

import org.example.domain.Doctor;

import java.util.List;
import java.util.Optional;

public interface DoctorDAO {
    List<Doctor> findAvailable();


    Optional<Doctor> findByIdNumber(String idNumber);

    void save(Doctor doctor);
}
