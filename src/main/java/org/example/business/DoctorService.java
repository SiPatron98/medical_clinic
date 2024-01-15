package org.example.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.business.dao.DoctorDAO;
import org.example.domain.Doctor;
import org.example.domain.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class DoctorService {

    private final DoctorDAO doctorDAO;

    @Transactional
    public List<Doctor> findAvailable() {
        List<Doctor> availableDoctors = doctorDAO.findAvailable();
        log.info("Available doctors: [{}]", availableDoctors.size());
        return availableDoctors;
    }

    @Transactional
    public Doctor findDoctor(String idNumber) {
        Optional<Doctor> doctor = doctorDAO.findByIdNumber(idNumber);
        if (doctor.isEmpty()) {
            throw new NotFoundException("Could not find doctor by idNumber: [%s]".formatted(idNumber));
        }
        return doctor.get();
    }

    @Transactional
    public void save(Doctor doctor) {
        doctorDAO.save(doctor);
    }
}
