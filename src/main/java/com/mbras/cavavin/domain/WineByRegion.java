package com.mbras.cavavin.domain;

import java.io.Serializable;

/**
 * Created by Mael on 12/02/2017.
 */
public class WineByRegion implements Serializable {

    private String region;

    private Long sum;

    public WineByRegion(String region, Long sum) {
        this.region = region;
        this.sum = sum;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Long getSum() {
        return sum;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }
}
