package com.mbras.cavavin.service.dto;

import com.mbras.cavavin.domain.WineByColor;
import com.mbras.cavavin.domain.WineByRegion;
import com.mbras.cavavin.domain.WineByYear;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the Cellar entity.
 */
public class CellarDTO implements Serializable {

    private Long id;

    private Integer capacity;

    private Long userId;

    private String userLogin;

    private Long sumOfWine;

    private List<WineByRegion> wineByRegion;

    private List<WineByColor> wineByColor;

    private List<WineByYear> wineByYear;

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

    public Long getSumOfWine() {
        return sumOfWine;
    }

    public void setSumOfWine(Long sumOfWine) {
        this.sumOfWine = sumOfWine;
    }

    public List<WineByRegion> getWineByRegion() {
        return wineByRegion;
    }

    public void setWineByRegion(List<WineByRegion> wineByRegion) {
        this.wineByRegion = wineByRegion;
    }

    public List<WineByColor> getWineByColor() {
        return wineByColor;
    }

    public void setWineByColor(List<WineByColor> wineByColor) {
        this.wineByColor = wineByColor;
    }

    public List<WineByYear> getWineByYear() {
        return wineByYear;
    }

    public void setWineByYear(List<WineByYear> wineByYear) {
        this.wineByYear = wineByYear;
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

        return !Objects.equals(id, cellarDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CellarDTO{" +
            "id=" + id +
            ", capacity=" + capacity +
            ", userId=" + userId +
            ", sumOfWine=" + sumOfWine +
            ", wineByRegion=" + wineByRegion +
            ", wineByColor=" + wineByColor +
            ", wineByYear=" + wineByYear +
            '}';
    }
}
