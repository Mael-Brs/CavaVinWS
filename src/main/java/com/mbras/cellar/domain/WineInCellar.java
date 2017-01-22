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

    @Column(name = "min_keep")
    private Integer minKeep;

    @Column(name = "max_keep")
    private Integer maxKeep;

    @Column(name = "price")
    private Double price;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "comments")
    private String comments;

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

    public Integer getMinKeep() {
        return minKeep;
    }

    public WineInCellar minKeep(Integer minKeep) {
        this.minKeep = minKeep;
        return this;
    }

    public void setMinKeep(Integer minKeep) {
        this.minKeep = minKeep;
    }

    public Integer getMaxKeep() {
        return maxKeep;
    }

    public WineInCellar maxKeep(Integer maxKeep) {
        this.maxKeep = maxKeep;
        return this;
    }

    public void setMaxKeep(Integer maxKeep) {
        this.maxKeep = maxKeep;
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

    public String getComments() {
        return comments;
    }

    public WineInCellar comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
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
            ", minKeep='" + minKeep + "'" +
            ", maxKeep='" + maxKeep + "'" +
            ", price='" + price + "'" +
            ", quantity='" + quantity + "'" +
            ", comments='" + comments + "'" +
            '}';
    }
}
