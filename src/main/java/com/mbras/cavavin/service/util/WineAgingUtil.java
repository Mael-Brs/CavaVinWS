package com.mbras.cavavin.service.util;

import com.mbras.cavavin.domain.RegionEnum;
import com.mbras.cavavin.domain.Wine;
import com.mbras.cavavin.domain.WineInCellar;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mael on 26/03/2017.
 */
public final class WineAgingUtil {

    private WineAgingUtil() {
    }

    private static final Map<String, Integer> redMap;
    static {
        redMap = new HashMap<>();
        redMap.put(RegionEnum.BORDEAUX.getValue(), 10);
        redMap.put(RegionEnum.LOIRE.getValue(), 6);
        redMap.put(RegionEnum.SUD_OUEST.getValue(), 10);
        redMap.put(RegionEnum.BOURGOGNE.getValue(), 10);
        redMap.put(RegionEnum.RHONE.getValue(), 6);
        redMap.put(RegionEnum.BEAUJOLAIS.getValue(), 4);
        redMap.put(RegionEnum.PROVENCE.getValue(),  8);
        redMap.put(RegionEnum.LANGUEDOC.getValue(), 8);
        redMap.put(RegionEnum.ROUSSILLON.getValue(), 8);
    }

    private static final Map<String, Integer> whiteMap;
    static {
        whiteMap = new HashMap<>();
        whiteMap.put(RegionEnum.BORDEAUX.getValue(), 8);
        whiteMap.put(RegionEnum.LOIRE.getValue(), 4);
        whiteMap.put(RegionEnum.SUD_OUEST.getValue(), 5);
        whiteMap.put(RegionEnum.BOURGOGNE.getValue(), 4);
        whiteMap.put(RegionEnum.RHONE.getValue(), 4);
        whiteMap.put(RegionEnum.BEAUJOLAIS.getValue(), 4);
        whiteMap.put(RegionEnum.PROVENCE.getValue(),  3);
        whiteMap.put(RegionEnum.LANGUEDOC.getValue(), 3);
        whiteMap.put(RegionEnum.ROUSSILLON.getValue(), 3);
        whiteMap.put(RegionEnum.JURA.getValue(), 10);
        whiteMap.put(RegionEnum.ALSACE.getValue(), 5);
        whiteMap.put(RegionEnum.CHAMPAGNE.getValue(), 3);
    }

    private static final Map<String, Integer> pinkMap;
    static {
        pinkMap = new HashMap<>();
        pinkMap.put(RegionEnum.LANGUEDOC.getValue(), 3);
        pinkMap.put(RegionEnum.PROVENCE.getValue(), 3);
        pinkMap.put(RegionEnum.RHONE.getValue(), 2);
    }

    public static WineInCellar setMaxKeep(WineInCellar wineInCellar){
        Wine wine = wineInCellar.getVintage().getWine();
        if(wine != null && "Rouge".equals(wine.getColor().getColorName())){
            wineInCellar.setMaxKeep(redMap.getOrDefault(wine.getRegion().getRegionName(),6));
        } else if(wine != null && "Blanc".equals(wine.getColor().getColorName())){
            wineInCellar.setMaxKeep(whiteMap.getOrDefault(wine.getRegion().getRegionName(),3));
        }else if(wine != null && "Rose".equals(wine.getColor().getColorName())){
            wineInCellar.setMaxKeep(pinkMap.getOrDefault(wine.getRegion().getRegionName(),3));
        }
        return wineInCellar;
    }
}
