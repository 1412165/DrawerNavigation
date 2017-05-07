package com.nthieuitus.findplaces.Architecture;

import com.google.android.gms.maps.model.LatLng;
import com.nthieuitus.findplaces.Model.MyLocation;

/**
 * Created by Nguyen Trung Hieu on 06-May-17.
 */

public class PlaceInfo {
    public String googlePlaceId;
    public int placeId;
    public String name;
    public String address;
    public double lat, lng;
    public String website;
    public String phoneNumber;
    public String internationalPhoneNumber;
    public String rating;
    public String keyword;

    public PlaceInfo() {

    }

    public PlaceInfo(String googlePlaceId, int placeId, String name, String address, double lat, double lng, String website, String phoneNumber, String internationalPhoneNumber, String keyword) {
        this.googlePlaceId = googlePlaceId;
        this.placeId = placeId;
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.website = website;
        this.phoneNumber = phoneNumber;
        this.internationalPhoneNumber = internationalPhoneNumber;
        this.keyword = keyword;
    }

    public String getStandardPlaceId() {
        if (placeId <= 0)
            return googlePlaceId;
        return String.valueOf(placeId);
    }

    public LatLng toLatLng() {
        return new LatLng(lat, lng);
    }
    public MyLocation toMyLocation() {
        return new MyLocation(lat, lng);
    }
}
