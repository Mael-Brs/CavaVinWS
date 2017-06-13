package com.mbras.cavavin.service.mapper;

import com.mbras.cavavin.domain.*;
import com.mbras.cavavin.service.dto.PinnedVintageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PinnedVintage and its DTO PinnedVintageDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class })
public interface PinnedVintageMapper extends EntityMapper <PinnedVintageDTO, PinnedVintage> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")

    PinnedVintageDTO toDto(PinnedVintage pinnedVintage);

    @Mapping(source = "userId", target = "user")

    PinnedVintage toEntity(PinnedVintageDTO pinnedVintageDTO);
    default PinnedVintage fromId(Long id) {
        if (id == null) {
            return null;
        }
        PinnedVintage pinnedVintage = new PinnedVintage();
        pinnedVintage.setId(id);
        return pinnedVintage;
    }
}
