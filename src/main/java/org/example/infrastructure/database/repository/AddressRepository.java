package org.example.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.example.business.dao.AddressDAO;
import org.example.business.dao.AppointmentDAO;
import org.example.domain.Address;
import org.example.domain.Appointment;
import org.example.domain.Calendar;
import org.example.domain.Disease;
import org.example.infrastructure.database.entity.*;
import org.example.infrastructure.database.repository.jpa.AddressJpaRepository;
import org.example.infrastructure.database.repository.jpa.AppointmentJpaRepository;
import org.example.infrastructure.database.repository.mapper.AddressEntityMapper;
import org.example.infrastructure.database.repository.mapper.AppointmentEntityMapper;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class AddressRepository implements AddressDAO {

    private final AddressJpaRepository addressJpaRepository;
    private final AddressEntityMapper addressEntityMapper;


    @Override
    public Address findById(Integer id) {
        return addressEntityMapper.map(addressJpaRepository.findById(id).get());
    }
}
