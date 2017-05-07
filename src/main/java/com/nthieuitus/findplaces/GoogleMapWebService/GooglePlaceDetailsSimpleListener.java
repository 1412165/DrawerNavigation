package com.nthieuitus.findplaces.GoogleMapWebService;

import com.nthieuitus.findplaces.Architecture.PlaceInfo;

/**
 * Created by Nguyen Trung Hieu on 06-May-17.
 */

public interface GooglePlaceDetailsSimpleListener {
    void onGooglePlaceDetailsSimpleStart();
    void onGooglePlaceDetailsSimpleFailed();
    void onGooglePlaceDetailsSimpleSuccess(PlaceInfo place);
}
