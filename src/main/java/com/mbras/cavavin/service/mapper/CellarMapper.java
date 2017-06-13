package com.mbras.cavavin.service.mapper;

import com.mbras.cavavin.domain.*;
import com.mbras.cavavin.service.dto.CellarDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Cellar and its DTO CellarDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface CellarMapper extends EntityMapper <CellarDTO, Cellar> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    CellarDTO toDto(Cellar cellar); 

    @Mapping(source = "userId", target = "user")
    Cellar toEntity(CellarDTO cellarDTO); 
    default Cellar fromId(Long id) {
        if (id == null) {
            return null;
        }
        Cellar cellar = new Cellar();
        cellar.setId(id);
        return cellar;
    }
}
