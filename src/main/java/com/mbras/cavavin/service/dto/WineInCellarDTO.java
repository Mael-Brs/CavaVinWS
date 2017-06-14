package com.mbras.cavavin.service.dto;


import com.mbras.cavavin.domain.Vintage;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the WineInCellar entity.
 */
public class WineInCellarDTO implements Serializable {

    private Long id;

    private Integer minKeep;

    private Integer maxKeep;

    private Double price;

    @NotNull
    private Integer quantity;

    private String comments;

    private Vintage vintage;

    private Long cellarId;

    private Integer apogee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMinKeep() {
        return minKeep;
    }

    public void setMinKeep(Integer minKeep) {
        this.minKeep = minKeep;
    }

    public Integer getMaxKeep() {
        return maxKeep;
    }

    public void setMaxKeep(Integer maxKeep) {
        this.maxKeep = maxKeep;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Vintage getVintage() {
        return vintage;
    }

    public void setVintage(Vintage vintage) {
        this.vintage = vintage;
    }

    public Long getCellarId() {
        return cellarId;
    }

    public void setCellarId(Long cellarId) {
        this.cellarId = cellarId;
    }

    public Integer getApogee() {
        return apogee;
    }

    public void setApogee(Integer apogee) {
        this.apogee = apogee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WineInCellarDTO wineInCellarDTO = (WineInCellarDTO) o;
        if(wineInCellarDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wineInCellarDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WineInCellarDTO{" +
            "id=" + getId() +
            ", minKeep='" + getMinKeep() + "'" +
            ", maxKeep='" + getMaxKeep() + "'" +
            ", price='" + getPrice() + "'" +
            ", quantity='" + getQuantity() + "'" +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
