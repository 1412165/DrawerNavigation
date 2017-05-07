package com.nthieuitus.findplaces.GoogleMapWebService;

import com.nthieuitus.findplaces.Model.PlaceList;

/**
 * Created by Nguyen Trung Hieu on 06-May-17.
 */

public interface GooglePlaceSearchNearBySearchListener {
    void onGooglePlaceSearchStart();
    void onGooglePlaceSearchSuccess(PlaceList placeList);
}
