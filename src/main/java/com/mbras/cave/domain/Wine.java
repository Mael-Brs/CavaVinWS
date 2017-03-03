package com.mbras.cave.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Wine.
 */
@Entity
@Table(name = "wine")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "wine")
public class Wine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "appellation")
    private String appellation;

    @Column(name = "year")
    private Integer year;

    @Column(name = "max_keep")
    private Integer maxKeep;

    @Column(name = "bare_code")
    private Integer bareCode;

    @ManyToOne
    private Region region;

    @ManyToOne
    private Color color;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Wine name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppellation() {
        return appellation;
    }

    public Wine appellation(String appellation) {
        this.appellation = appellation;
        return this;
    }

    public void setAppellation(String appellation) {
        this.appellation = appellation;
    }

    public Integer getYear() {
        return year;
    }

    public Wine year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMaxKeep() {
        return maxKeep;
    }

    public Wine maxKeep(Integer maxKeep) {
        this.maxKeep = maxKeep;
        return this;
    }

    public void setMaxKeep(Integer maxKeep) {
        this.maxKeep = maxKeep;
    }

    public Integer getBareCode() {
        return bareCode;
    }

    public Wine bareCode(Integer bareCode) {
        this.bareCode = bareCode;
        return this;
    }

    public void setBareCode(Integer bareCode) {
        this.bareCode = bareCode;
    }

    public Region getRegion() {
        return region;
    }

    public Wine region(Region region) {
        this.region = region;
        return this;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Color getColor() {
        return color;
    }

    public Wine color(Color color) {
        this.color = color;
        return this;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Wine wine = (Wine) o;
        if(wine.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, wine.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Wine{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", appellation='" + appellation + "'" +
            ", year='" + year + "'" +
            ", maxKeep='" + maxKeep + "'" +
            ", bareCode='" + bareCode + "'" +
            '}';
    }
}
