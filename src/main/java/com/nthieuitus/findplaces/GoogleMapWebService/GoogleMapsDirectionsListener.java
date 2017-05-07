package com.nthieuitus.findplaces.GoogleMapWebService;

import com.nthieuitus.findplaces.Model.Route;

import java.util.List;

/**
 * Created by Nguyen Trung Hieu on 06-May-17.
 */

public interface GoogleMapsDirectionsListener {
    void onGoogleMapsDirectionsStart();
    void onGoogleMapsDirectionsSuccess(List<Route> routes);
}
