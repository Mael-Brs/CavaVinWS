package com.mbras.cavavin.domain;

/**
 * Created by Mael on 26/03/2017.
 */
public enum RegionEnum {

    BORDEAUX("Bordeaux"), LOIRE("Vallée de la Loire"), SUD_OUEST("Sud-Ouest"),
    BOURGOGNE("Bourgogne"), RHONE("Bourgogne"), BEAUJOLAIS("Beaujolais"),
    PROVENCE("Provence"), LANGUEDOC("Languedoc"), ROUSSILLON("Roussillon"),
    JURA("Jura"), ALSACE("Alsace"), CHAMPAGNE("Champagne");

    private String value;

    RegionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
