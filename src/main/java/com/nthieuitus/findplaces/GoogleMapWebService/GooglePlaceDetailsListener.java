package com.nthieuitus.findplaces.GoogleMapWebService;

import com.nthieuitus.findplaces.Model.Place;

/**
 * Created by Nguyen Trung Hieu on 06-May-17.
 */

public interface GooglePlaceDetailsListener {
    void onGooglePlaceDetailsStart();
    void onGooglePlaceDetailsSuccess(Place place);
}
