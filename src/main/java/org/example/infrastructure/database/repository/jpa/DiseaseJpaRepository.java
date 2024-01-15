package org.example.infrastructure.database.repository.jpa;

import org.example.infrastructure.database.entity.DiseaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiseaseJpaRepository extends JpaRepository<DiseaseEntity, Integer> {
}
