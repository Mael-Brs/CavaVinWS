package com.mbras.cavavin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
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

    @Column(name = "bare_code")
    private Integer bareCode;

    @ManyToOne(fetch=FetchType.EAGER)
    private Year year;

    @ManyToOne(fetch=FetchType.EAGER)
    private Wine wine;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Year getYear() {
        return year;
    }

    public Vintage year(Year year) {
        this.year = year;
        return this;
    }

    public void setYear(Year year) {
        this.year = year;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vintage vintage = (Vintage) o;
        if (vintage.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, vintage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Vintage{" +
            "id=" + id +
            ", bareCode='" + bareCode + "'" +
            '}';
    }
}
