package com.mbras.cavavin.domain;

import java.io.Serializable;

/**
 * Created by Mael on 27/03/2017.
 */
public class WineByYear implements Serializable {

    private Integer year;

    private Long sum;

    public WineByYear(Integer year, Long sum) {
        this.year = year;
        this.sum = sum;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Long getSum() {
        return sum;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }
}
