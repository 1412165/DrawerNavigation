package com.nthieuitus.findplaces.Architecture;

import com.nthieuitus.findplaces.Model.GlobalVars;
import com.nthieuitus.findplaces.Model.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nguyen Trung Hieu on 06-May-17.
 */

public class GooglePlaceImporter extends PlaceImporter{

    @Override
    public PlaceInfo Convert(Object input) {
        Place info =(Place)input;
        PlaceInfo result = new PlaceInfo();
        result.googlePlaceId = info.mPlaceId;
        result.placeId = -1;
        result.name = info.mName;
        result.address = info.mVicinity;
        result.website = info.mPlaceDetails != null ? info.mPlaceDetails.website : null;
        result.phoneNumber = info.mPlaceDetails != null ? info.mPlaceDetails.localPhoneNumber : null;
        result.internationalPhoneNumber = info.mPlaceDetails != null ? info.mPlaceDetails.internationalPhoneNumber : null;
        result.lat = info.mLocation.latitude;
        result.lng = info.mLocation.longitude;
        result.rating = info.mRating;
        result.keyword = GlobalVars.keywordItem.text;
        return result;
    }

    @Override
    public boolean IsValidInput(Object input) {
        try {
            Place info = (Place) input;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public List<PlaceInfo> ConvertList(Object places) {
        List<PlaceInfo> res = new ArrayList<PlaceInfo>();

        ArrayList<Object> placeObjects = (ArrayList<Object>)places;
        for (Object place : placeObjects) {
            if (IsValidInput(place)) {
                res.add(Convert(place));
            }
        }
        return res;
    }
}
