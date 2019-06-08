package com.mbras.cavavin.service.dto;


import com.sun.istack.internal.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Cellar entity.
 */
public class CellarDTO implements Serializable {

    private Long id;

    private Integer capacity;

    @NotNull
    private Long userId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CellarDTO cellarDTO = (CellarDTO) o;
        if(cellarDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cellarDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CellarDTO{" +
            "id=" + getId() +
            ", capacity=" + getCapacity() +
            ", userId=" + getUserId() +
            "}";
    }
}
