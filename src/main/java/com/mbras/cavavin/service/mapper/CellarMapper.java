package com.mbras.cavavin.service.mapper;

import com.mbras.cavavin.domain.*;
import com.mbras.cavavin.service.dto.CellarDTO;

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
