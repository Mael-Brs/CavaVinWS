package com.mbras.cellar.domain;

/**
 * Created by Mael on 12/02/2017.
 */
public class WineByColor {

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
