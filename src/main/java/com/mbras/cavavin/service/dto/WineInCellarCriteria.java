package com.mbras.cavavin.service.dto;

import io.github.jhipster.service.filter.*;

import java.io.Serializable;






/**
 * Criteria class for the WineInCellar entity. This class is used in WineInCellarResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /wine-in-cellars?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WineInCellarCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private IntegerFilter minKeep;

    private IntegerFilter maxKeep;

    private DoubleFilter price;

    private IntegerFilter quantity;

    private StringFilter comments;

    private StringFilter location;

    private LongFilter cellarId;

    private IntegerFilter childYear;

    private IntegerFilter apogeeYear;

    private LongFilter vintageId;

    private StringFilter region;

    private StringFilter color;

    private String keywords;

    public WineInCellarCriteria() {
        //default constructor
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getMinKeep() {
        return minKeep;
    }

    public void setMinKeep(IntegerFilter minKeep) {
        this.minKeep = minKeep;
    }

    public IntegerFilter getMaxKeep() {
        return maxKeep;
    }

    public void setMaxKeep(IntegerFilter maxKeep) {
        this.maxKeep = maxKeep;
    }

    public DoubleFilter getPrice() {
        return price;
    }

    public void setPrice(DoubleFilter price) {
        this.price = price;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public StringFilter getComments() {
        return comments;
    }

    public void setComments(StringFilter comments) {
        this.comments = comments;
    }

    public StringFilter getLocation() {
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public LongFilter getCellarId() {
        return cellarId;
    }

    public void setCellarId(LongFilter cellarId) {
        this.cellarId = cellarId;
    }

    public IntegerFilter getChildYear() {
        return childYear;
    }

    public void setChildYear(IntegerFilter childYear) {
        this.childYear = childYear;
    }

    public IntegerFilter getApogeeYear() {
        return apogeeYear;
    }

    public void setApogeeYear(IntegerFilter apogeeYear) {
        this.apogeeYear = apogeeYear;
    }

    public LongFilter getVintageId() {
        return vintageId;
    }

    public void setVintageId(LongFilter vintageId) {
        this.vintageId = vintageId;
    }

    public StringFilter getRegion() {
        return region;
    }

    public void setRegion(StringFilter region) {
        this.region = region;
    }

    public StringFilter getColor() {
        return color;
    }

    public void setColor(StringFilter color) {
        this.color = color;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return "WineInCellarCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (minKeep != null ? "minKeep=" + minKeep + ", " : "") +
                (maxKeep != null ? "maxKeep=" + maxKeep + ", " : "") +
                (price != null ? "price=" + price + ", " : "") +
                (quantity != null ? "quantity=" + quantity + ", " : "") +
                (comments != null ? "comments=" + comments + ", " : "") +
                (location != null ? "location=" + location + ", " : "") +
                (cellarId != null ? "cellarId=" + cellarId + ", " : "") +
                (childYear != null ? "childYear=" + childYear + ", " : "") +
                (apogeeYear != null ? "apogeeYear=" + apogeeYear + ", " : "") +
                (vintageId != null ? "vintageId=" + vintageId + ", " : "") +
            "}";
    }

}
