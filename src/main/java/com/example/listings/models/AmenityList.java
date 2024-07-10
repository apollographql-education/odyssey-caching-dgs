package com.example.listings.models;

import com.example.listings.generated.types.Amenity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AmenityList {

    public List<Amenity> getAmenityList() {
        return amenityList;
    }

    @JsonSetter("amenities")
    public void setAmenityList(List<Amenity> amenityList) {
        this.amenityList = amenityList;
    }

    public List<Amenity> amenityList;
}
