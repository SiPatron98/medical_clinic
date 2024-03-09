package org.example.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.business.dao.PatientDAO;
import org.example.domain.Patient;
import org.example.domain.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class PatientService {

    private final PatientDAO patientDAO;

    @Transactional
    public List<Patient> findAvailable() {
        List<Patient> availablePatients = patientDAO.findAvailable();
        log.info("Available patients: [{}]", availablePatients.size());
        return availablePatients;
    }

    @Transactional
    public Patient findPatient(String pesel) {
        Optional<Patient> patient = patientDAO.findByPesel(pesel);
        if (patient.isEmpty()) {
            throw new NotFoundException("Could not find patient by pesel: [%s]".formatted(pesel));
        }
        return patient.get();
    }

    @Transactional
    public void save(Patient patient) {
        patientDAO.save(patient);
    }
}
