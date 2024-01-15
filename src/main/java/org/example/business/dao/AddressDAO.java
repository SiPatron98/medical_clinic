package org.example.business.dao;

import org.example.domain.Address;
import org.example.domain.Appointment;

import java.util.List;
import java.util.Set;

public interface AddressDAO {

    Address findById(Integer id);
}
