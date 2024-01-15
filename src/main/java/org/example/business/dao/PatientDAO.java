package org.example.business.dao;

import org.example.domain.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientDAO {
    List<Patient> findAvailable();

    Optional<Patient> findByPesel(String pesel);

    void save(Patient patient);
}
