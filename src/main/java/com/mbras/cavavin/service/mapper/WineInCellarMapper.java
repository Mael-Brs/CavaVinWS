package com.mbras.cavavin.service.mapper;

import com.mbras.cavavin.domain.*;
import com.mbras.cavavin.service.dto.WineInCellarDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity WineInCellar and its DTO WineInCellarDTO.
 */
@Mapper(componentModel = "spring", uses = {CellarMapper.class, VintageMapper.class, })
public interface WineInCellarMapper extends EntityMapper <WineInCellarDTO, WineInCellar> {

    @Mapping(source = "cellar.id", target = "cellarId")

    @Mapping(source = "vintage.id", target = "vintageId")
    WineInCellarDTO toDto(WineInCellar wineInCellar); 

    @Mapping(source = "cellarId", target = "cellar")

    @Mapping(source = "vintageId", target = "vintage")
    WineInCellar toEntity(WineInCellarDTO wineInCellarDTO); 
    default WineInCellar fromId(Long id) {
        if (id == null) {
            return null;
        }
        WineInCellar wineInCellar = new WineInCellar();
        wineInCellar.setId(id);
        return wineInCellar;
    }
}
