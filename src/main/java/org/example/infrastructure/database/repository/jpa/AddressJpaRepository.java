package org.example.infrastructure.database.repository.jpa;

import org.example.infrastructure.database.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressJpaRepository extends JpaRepository<AddressEntity, Integer> {
}
