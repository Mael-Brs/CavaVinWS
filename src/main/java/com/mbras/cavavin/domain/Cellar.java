package com.mbras.cavavin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A Cellar.
 */
@Entity
@Table(name = "cellar")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "cellar")
public class Cellar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "capacity")
    private Integer capacity;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @Transient
    private Long sumOfWine;

    @Transient
    private List<WineByRegion> wineByRegion;

    @Transient
    private List<WineByColor> wineByColor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Cellar capacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public User getUser() {
        return user;
    }

    public Cellar user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getSumOfWine() {
        return sumOfWine;
    }

    public void setSumOfWine(Long sumOfWine) {
        this.sumOfWine = sumOfWine;
    }

    public List<WineByRegion> getWineByRegion() {
        return wineByRegion;
    }

    public void setWineByRegion(List<WineByRegion> wineByRegion) {
        this.wineByRegion = wineByRegion;
    }

    public List<WineByColor> getWineByColor() {
        return wineByColor;
    }

    public void setWineByColor(List<WineByColor> wineByColor) {
        this.wineByColor = wineByColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cellar cellar = (Cellar) o;
        if (cellar.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cellar.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Cellar{" +
            "id=" + getId() +
            ", capacity='" + getCapacity() + "'" +
            "}";
    }
}
