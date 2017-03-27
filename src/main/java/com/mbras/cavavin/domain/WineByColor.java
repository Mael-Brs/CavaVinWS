package com.mbras.cavavin.domain;

import java.io.Serializable;

/**
 * Created by Mael on 12/02/2017.
 */
public class WineByColor implements Serializable {

    private String color;

    private Long sum;

    public WineByColor(String color, Long sum) {
        this.color = color;
        this.sum = sum;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getSum() {
        return sum;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }
}
