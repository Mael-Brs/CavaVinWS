package com.mbras.cavavin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PinnedWine.
 */
@Entity
@Table(name = "pinned_wine")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "pinnedwine")
public class PinnedWine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(optional = false)
    @NotNull
    private Wine wine;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public PinnedWine userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Wine getWine() {
        return wine;
    }

    public PinnedWine wine(Wine wine) {
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
        PinnedWine pinnedWine = (PinnedWine) o;
        if (pinnedWine.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pinnedWine.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PinnedWine{" +
            "id=" + getId() +
            ", userId='" + getUserId() + "'" +
            "}";
    }
}
