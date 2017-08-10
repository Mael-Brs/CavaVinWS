package com.mbras.cavavin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "min_keep")
    private Integer minKeep;

    @Column(name = "max_keep")
    private Integer maxKeep;

    @Column(name = "price")
    private Double price;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "comments")
    private String comments;

    @NotNull
    @Column(name = "cellar_id", nullable = false)
    private Long cellarId;

    @ManyToOne(optional = false)
    @NotNull
    private Vintage vintage;

    @Transient
    private Integer apogee;

    @Column(name = "location")
    private String location;

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
        if(maxKeep != null && this.vintage != null && this.vintage.getYear() != null){
            this.apogee = this.vintage.getYear().getNumber() + maxKeep;
        }
        return this;
    }

    public void setMaxKeep(Integer maxKeep) {
        this.maxKeep = maxKeep;
        if(maxKeep != null && this.vintage != null && this.vintage.getYear() != null){
            this.apogee = this.vintage.getYear().getNumber() + maxKeep;
        }
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

    public Long getCellarId() {
        return cellarId;
    }

    public WineInCellar cellarId(Long cellarId) {
        this.cellarId = cellarId;
        return this;
    }

    public void setCellarId(Long cellarId) {
        this.cellarId = cellarId;
    }

    public Vintage getVintage() {
        return vintage;
    }

    public WineInCellar vintage(Vintage vintage) {
        this.vintage = vintage;
        if(this.maxKeep != null && vintage != null && vintage.getYear() != null){
            this.apogee = vintage.getYear().getNumber() + this.maxKeep;
        }
        return this;
    }

    public void setVintage(Vintage vintage) {
        this.vintage = vintage;
        if(this.maxKeep != null && vintage != null && vintage.getYear() != null){
            this.apogee = vintage.getYear().getNumber() + this.maxKeep;
        }
    }

    public Integer getApogee() {
        return apogee;
    }

    @PostLoad
    public void setApogee(){
        if(this.maxKeep != null && this.vintage != null && this.vintage.getYear() != null){
            this.apogee = this.vintage.getYear().getNumber() + this.maxKeep;
        }
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
        if (wineInCellar.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wineInCellar.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WineInCellar{" +
            "id=" + getId() +
            ", minKeep='" + getMinKeep() + "'" +
            ", maxKeep='" + getMaxKeep() + "'" +
            ", price='" + getPrice() + "'" +
            ", quantity='" + getQuantity() + "'" +
            ", comments='" + getComments() + "'" +
            ", cellarId='" + getCellarId() + "'" +
            "}";
    }
}
