package com.mbras.cellar.service.mapper;

import com.mbras.cellar.domain.*;
import com.mbras.cellar.service.dto.WineInCellarDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity WineInCellar and its DTO WineInCellarDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WineInCellarMapper {

    @Mapping(source = "cellar.id", target = "cellarId")
    @Mapping(source = "vintage.id", target = "vintageId")
    WineInCellarDTO wineInCellarToWineInCellarDTO(WineInCellar wineInCellar);

    List<WineInCellarDTO> wineInCellarsToWineInCellarDTOs(List<WineInCellar> wineInCellars);

    @Mapping(source = "cellarId", target = "cellar")
    @Mapping(source = "vintageId", target = "vintage")
    WineInCellar wineInCellarDTOToWineInCellar(WineInCellarDTO wineInCellarDTO);

    List<WineInCellar> wineInCellarDTOsToWineInCellars(List<WineInCellarDTO> wineInCellarDTOs);

    default Cellar cellarFromId(Long id) {
        if (id == null) {
            return null;
        }
        Cellar cellar = new Cellar();
        cellar.setId(id);
        return cellar;
    }

    default Vintage vintageFromId(Long id) {
        if (id == null) {
            return null;
        }
        Vintage vintage = new Vintage();
        vintage.setId(id);
        return vintage;
    }
}
