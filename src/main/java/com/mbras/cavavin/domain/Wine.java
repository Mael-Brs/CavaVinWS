package com.mbras.cavavin.domain;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "appellation")
    private String appellation;

    @NotNull
    @Column(name = "producer", nullable = false)
    private String producer;

    @NotNull
    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

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

    public String getProducer() {
        return producer;
    }

    public Wine producer(String producer) {
        this.producer = producer;
        return this;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public Wine creatorId(Long creatorId) {
        this.creatorId = creatorId;
        return this;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
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
        if (wine.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wine.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Wine{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", appellation='" + getAppellation() + "'" +
            ", producer='" + getProducer() + "'" +
            ", creatorId='" + getCreatorId() + "'" +
            "}";
    }
}
