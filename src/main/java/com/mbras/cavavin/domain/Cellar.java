package com.mbras.cavavin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A Cellar.
 */
@Entity
@Table(name = "cellar")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Cellar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "capacity")
    private Integer capacity;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Transient
    private Long sumOfWine;

    @Transient
    private List<WineByRegion> wineByRegion;

    @Transient
    private List<WineByColor> wineByColor;

    @Transient
    private List<WineByYear> wineByYear;

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

    public Long getUserId() {
        return userId;
    }

    public Cellar userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public List<WineByYear> getWineByYear() {
        return wineByYear;
    }

    public void setWineByYear(List<WineByYear> wineByYear) {
        this.wineByYear = wineByYear;
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
            "id=" + id +
            ", capacity=" + capacity +
            ", userId=" + userId +
            ", sumOfWine=" + sumOfWine +
            ", wineByRegion=" + wineByRegion +
            ", wineByColor=" + wineByColor +
            ", wineByYear=" + wineByYear +
            '}';
    }
}
