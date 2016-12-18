package com.mbras.cellar.service.dto;

import com.mbras.cellar.domain.Vintage;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the WineInCellar entity.
 */
public class WineInCellarDTO implements Serializable {

    private Long id;

    private Double price;

    private Integer quantity;

    private Vintage vintage;

    private Long cellarId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WineInCellarDTO wineInCellarDTO = (WineInCellarDTO) o;

        if ( ! Objects.equals(id, wineInCellarDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WineInCellarDTO{" +
            "id=" + id +
            ", price='" + price + "'" +
            ", quantity='" + quantity + "'" +
            '}';
    }
}
