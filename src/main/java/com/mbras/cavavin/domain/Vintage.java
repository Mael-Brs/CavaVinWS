package com.mbras.cavavin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A Vintage.
 */
@Entity
@Table(name = "vintage")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "vintage")
public class Vintage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "jhi_year", nullable = false)
    private Integer year;

    @Column(name = "bare_code")
    private Integer bareCode;

    @ManyToOne(optional = false)
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
            ", bareCode=" + getBareCode() +
            "}";
    }
}
