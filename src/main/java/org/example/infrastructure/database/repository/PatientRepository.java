package org.example.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.example.business.dao.PatientDAO;
import org.example.domain.Patient;
import org.example.infrastructure.database.repository.jpa.PatientJpaRepository;
import org.example.infrastructure.database.repository.mapper.PatientEntityMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PatientRepository implements PatientDAO {

    private final PatientJpaRepository patientJpaRepository;
    private final PatientEntityMapper patientEntityMapper;
    @Override
    public List<Patient> findAvailable() {
        return patientJpaRepository.findAll().stream()
                .map(patientEntityMapper::mapFromEntity)
                .toList();
    }

    @Override
    public Optional<Patient> findByPesel(String pesel) {
        return patientJpaRepository.findByPesel(pesel)
                .map(patientEntityMapper::mapFromEntity);
    }

    @Override
    public void save(Patient patient) {
        patientJpaRepository.saveAndFlush(patientEntityMapper.mapToEntity(patient));
    }
}
