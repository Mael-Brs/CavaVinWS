package com.mbras.cellar.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A WineInCellar.
 */
@Entity
@Table(name = "wine_in_cellar")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "wineincellar")
public class WineInCellar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "price")
    private Double price;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne
    private Cellar cellar;

    @ManyToOne
    private Vintage vintage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public WineInCellar price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public WineInCellar quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Cellar getCellar() {
        return cellar;
    }

    public WineInCellar cellar(Cellar cellar) {
        this.cellar = cellar;
        return this;
    }

    public void setCellar(Cellar cellar) {
        this.cellar = cellar;
    }

    public Vintage getVintage() {
        return vintage;
    }

    public WineInCellar vintage(Vintage vintage) {
        this.vintage = vintage;
        return this;
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
        WineInCellar wineInCellar = (WineInCellar) o;
        if (wineInCellar.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, wineInCellar.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WineInCellar{" +
            "id=" + id +
            ", price='" + price + "'" +
            ", quantity='" + quantity + "'" +
            '}';
    }
}
