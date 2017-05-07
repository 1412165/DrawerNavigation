package com.nthieuitus.findplaces.Model;

import com.google.android.gms.maps.model.Marker;
import com.nthieuitus.findplaces.Architecture.PlaceInfo;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Nguyen Trung Hieu on 06-May-17.
 */

public class GlobalVars {
    public enum DrawerType {KEYWORDS, SEARCH_PLACES, FAVORITE_PLACES};

    //public static MyLocation location = new MyLocation(15, 108);
    public static MyLocation location = new MyLocation(10.7629886, 106.6821975);
    public static PlaceInfo currentPlace;
    public static List<PlaceInfo> currentPlaceList;
    public static List<PlaceInfo> currentGooglePlaceList;
    public static List<PlaceInfo> currentMyServerPlaceList;

    public static HashMap<Marker, PlaceInfo> markerData = new HashMap<>();
    public static KeywordItem keywordItem;
    public static DrawerType drawer;
    public static String currentPhotoPath;

    public static boolean IsFakeGPS = false;
    public static MyLocation fakeLocation = new MyLocation(10.7629886, 106.6821975); //University of Science, District 5

    public static MyLocation getUserLocation() {
        if (IsFakeGPS) {
            return fakeLocation;
        } else {
            return location;
        }
    }
}
