package com.mbras.cavavin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PinnedVintage.
 */
@Entity
@Table(name = "pinned_vintage")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "pinnedvintage")
public class PinnedVintage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @NotNull
    private Vintage vintage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public PinnedVintage user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Vintage getVintage() {
        return vintage;
    }

    public PinnedVintage vintage(Vintage vintage) {
        this.vintage = vintage;
        return this;
    }

    public void setVintage(Vintage vintage) {
        this.vintage = vintage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PinnedVintage pinnedVintage = (PinnedVintage) o;
        if (pinnedVintage.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pinnedVintage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PinnedVintage{" +
            "id=" + getId() +
            "}";
    }
}
