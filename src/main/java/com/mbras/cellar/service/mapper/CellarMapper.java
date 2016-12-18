package com.mbras.cellar.service.mapper;

import com.mbras.cellar.domain.*;
import com.mbras.cellar.service.dto.CellarDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Cellar and its DTO CellarDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface CellarMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    CellarDTO cellarToCellarDTO(Cellar cellar);

    List<CellarDTO> cellarsToCellarDTOs(List<Cellar> cellars);

    @Mapping(source = "userId", target = "user")
    Cellar cellarDTOToCellar(CellarDTO cellarDTO);

    List<Cellar> cellarDTOsToCellars(List<CellarDTO> cellarDTOs);
}
