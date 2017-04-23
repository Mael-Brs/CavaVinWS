package com.mbras.cavavin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A WineAgingData.
 */
@Entity
@Table(name = "wine_aging_data")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "wineagingdata")
public class WineAgingData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "min_keep")
    private Integer minKeep;

    @Column(name = "max_keep")
    private Integer maxKeep;

    @ManyToOne
    private Color color;

    @ManyToOne
    private Region region;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMinKeep() {
        return minKeep;
    }

    public WineAgingData minKeep(Integer minKeep) {
        this.minKeep = minKeep;
        return this;
    }

    public void setMinKeep(Integer minKeep) {
        this.minKeep = minKeep;
    }

    public Integer getMaxKeep() {
        return maxKeep;
    }

    public WineAgingData maxKeep(Integer maxKeep) {
        this.maxKeep = maxKeep;
        return this;
    }

    public void setMaxKeep(Integer maxKeep) {
        this.maxKeep = maxKeep;
    }

    public Color getColor() {
        return color;
    }

    public WineAgingData color(Color color) {
        this.color = color;
        return this;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Region getRegion() {
        return region;
    }

    public WineAgingData region(Region region) {
        this.region = region;
        return this;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WineAgingData wineAgingData = (WineAgingData) o;
        if (wineAgingData.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, wineAgingData.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WineAgingData{" +
            "id=" + id +
            ", minKeep='" + minKeep + "'" +
            ", maxKeep='" + maxKeep + "'" +
            '}';
    }
}
