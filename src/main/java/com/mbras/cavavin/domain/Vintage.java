package com.mbras.cavavin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;


/**
 * A Vintage.
 */
@Entity
@Table(name = "vintage")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Vintage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "jhi_year", nullable = false)
    private Integer year;

    @Column(name = "child_year")
    private Integer childYear;

    @Column(name = "apogee_year")
    private Integer apogeeYear;

    @Column(name = "bare_code")
    private Integer bareCode;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @NotNull
    private Wine wine;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public Vintage year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getChildYear() {
        return childYear;
    }

    public Vintage childYear(Integer childYear) {
        this.childYear = childYear;
        return this;
    }

    public void setChildYear(Integer childYear) {
        this.childYear = childYear;
    }

    public Integer getApogeeYear() {
        return apogeeYear;
    }

    public Vintage apogeeYear(Integer apogeeYear) {
        this.apogeeYear = apogeeYear;
        return this;
    }

    public void setApogeeYear(Integer apogeeYear) {
        this.apogeeYear = apogeeYear;
    }

    public Integer getBareCode() {
        return bareCode;
    }

    public Vintage bareCode(Integer bareCode) {
        this.bareCode = bareCode;
        return this;
    }

    public void setBareCode(Integer bareCode) {
        this.bareCode = bareCode;
    }

    public Wine getWine() {
        return wine;
    }

    public Vintage wine(Wine wine) {
        this.wine = wine;
        return this;
    }

    public void setWine(Wine wine) {
        this.wine = wine;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vintage vintage = (Vintage) o;
        if (vintage.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vintage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Vintage{" +
            "id=" + getId() +
            ", year=" + getYear() +
            ", childYear=" + getChildYear() +
            ", apogeeYear=" + getApogeeYear() +
            ", bareCode=" + getBareCode() +
            "}";
    }
}
