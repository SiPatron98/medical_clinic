package org.example.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.business.dao.AddressDAO;
import org.example.domain.Address;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class AddressService {

    private final AddressDAO addressDAO;

    @Transactional
    public Address findById(Integer id) {
        return addressDAO.findById(id);
    }
}
