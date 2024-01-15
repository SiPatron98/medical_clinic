package org.example.api.dto.mapper;

import org.example.api.dto.DiseaseDTO;
import org.example.domain.Disease;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DiseaseMapper {

    DiseaseDTO map(final Disease disease);
}
