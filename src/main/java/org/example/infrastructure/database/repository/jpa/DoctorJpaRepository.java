package org.example.infrastructure.database.repository.jpa;

import org.example.infrastructure.database.entity.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorJpaRepository extends JpaRepository<DoctorEntity, Integer> {
    Optional<DoctorEntity> findByIdNumber(String idNumber);
}
