package com.mbras.cavavin.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the PinnedVintage entity.
 */
public class PinnedVintageDTO implements Serializable {

    private Long id;

    private Long userId;

    private String userLogin;

    private Long vintageId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getVintageId() {
        return vintageId;
    }

    public void setVintageId(Long vintageId) {
        this.vintageId = vintageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PinnedVintageDTO pinnedVintageDTO = (PinnedVintageDTO) o;
        if(pinnedVintageDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pinnedVintageDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PinnedVintageDTO{" +
            "id=" + getId() +
            "}";
    }
}
