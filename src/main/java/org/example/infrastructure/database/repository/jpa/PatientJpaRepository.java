package org.example.infrastructure.database.repository.jpa;

import org.example.infrastructure.database.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientJpaRepository extends JpaRepository<PatientEntity, Integer> {
    Optional<PatientEntity> findByPesel(String pesel);
}
