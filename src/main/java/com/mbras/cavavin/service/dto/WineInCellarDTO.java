package com.mbras.cavavin.service.dto;


import com.mbras.cavavin.domain.Vintage;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the WineInCellar entity.
 */
public class WineInCellarDTO implements Serializable {

    private Long id;

    private Integer childYear;

    private Integer apogeeYear;

    private Double price;

    @NotNull
    private Integer quantity;

    private String comments;

    private String location;

    @NotNull
    private Long cellarId;

    @NotNull
    private Vintage vintage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getChildYear() {
        return childYear;
    }

    public void setChildYear(Integer childYear) {
        this.childYear = childYear;
    }

    public Integer getApogeeYear() {
        return apogeeYear;
    }

    public void setApogeeYear(Integer apogeeYear) {
        this.apogeeYear = apogeeYear;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getCellarId() {
        return cellarId;
    }

    public void setCellarId(Long cellarId) {
        this.cellarId = cellarId;
    }

    public Vintage getVintage() {
        return vintage;
    }

    public void setVintage(Vintage vintage) {
        this.vintage = vintage;
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
            ", childYear=" + getChildYear() +
            ", apogeeYear=" + getApogeeYear() +
            ", price=" + getPrice() +
            ", quantity=" + getQuantity() +
            ", comments='" + getComments() + "'" +
            ", location='" + getLocation() + "'" +
            ", cellarId=" + getCellarId() +
            "}";
    }
}
