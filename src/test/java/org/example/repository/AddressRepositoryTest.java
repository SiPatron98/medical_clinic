package org.example.repository;

import org.example.domain.Address;
import org.example.infrastructure.database.entity.AddressEntity;
import org.example.infrastructure.database.repository.AddressRepository;
import org.example.infrastructure.database.repository.jpa.AddressJpaRepository;
import org.example.infrastructure.database.repository.mapper.AddressEntityMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AddressRepositoryTest {

    @InjectMocks
    private AddressRepository addressRepository;

    @Mock
    private AddressJpaRepository addressJpaRepository;

    @Mock
    private AddressEntityMapper addressEntityMapper;

    @Test
    void canAddressBeFound() {
        // given
        Integer id = 1;
        Address address = Address.builder()
                .addressId(1)
                .country("Poland")
                .city("Warsaw")
                .street("Malinowa")
                .postalCode("67-345")
                .patient(null)
                .build();
        AddressEntity addressEntity = AddressEntity.builder()
                .addressId(1)
                .country("Poland")
                .city("Warsaw")
                .street("Malinowa")
                .postalCode("67-345")
                .patient(null)
                .build();

        Mockito.when(addressJpaRepository.findById(id))
                .thenReturn(Optional.of(addressEntity));
        Mockito.when(addressEntityMapper.map(addressEntity))
                .thenReturn(address);
        // when
        Address result = addressRepository.findById(id);

        // then
        Assertions.assertEquals("Malinowa", result.getStreet());
    }
}
