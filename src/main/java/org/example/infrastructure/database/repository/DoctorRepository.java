package org.example.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.example.business.dao.DoctorDAO;
import org.example.domain.Doctor;
import org.example.infrastructure.database.repository.jpa.DoctorJpaRepository;
import org.example.infrastructure.database.repository.mapper.DoctorEntityMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class DoctorRepository implements DoctorDAO {

    private final DoctorJpaRepository doctorJpaRepository;
    private final DoctorEntityMapper doctorEntityMapper;
    @Override
    public List<Doctor> findAvailable() {
        return doctorJpaRepository.findAll().stream()
                .map(doctorEntityMapper::mapFromEntity)
                .toList();
    }

    @Override
    public Optional<Doctor> findByIdNumber(String idNumber) {
        return doctorJpaRepository.findByIdNumber(idNumber)
                .map(doctorEntityMapper::mapFromEntity);
    }

    @Override
    public void save(Doctor doctor) {
        doctorJpaRepository.saveAndFlush(doctorEntityMapper.mapToEntity(doctor));
    }
}
