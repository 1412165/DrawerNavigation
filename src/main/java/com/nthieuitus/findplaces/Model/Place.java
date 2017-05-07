package com.nthieuitus.findplaces.Model;

/**
 * Created by Nguyen Trung Hieu on 06-May-17.
 */

public class Place {
    public MyLocation mLocation;
    public String mIcon;
    public String mName;
    public String mPlaceId;
    public String mVicinity;
    public String mPhotoReference;
    public String mRating;
    public PlaceDetails mPlaceDetails;

    public Place(MyLocation mLocation, String mIcon, String mName, String mPlaceId, String mVicinity, String mPhotoReference, String mRating, PlaceDetails mPlaceDetails) {
        this.mLocation = mLocation;
        this.mIcon = mIcon;
        this.mName = mName;
        this.mPlaceId = mPlaceId;
        this.mVicinity = mVicinity;
        this.mPhotoReference = mPhotoReference;
        this.mRating = mRating;
        this.mPlaceDetails = mPlaceDetails;
    }

    @Override
    public String toString() {
        return "Place {" + "icon=" + mIcon + ", name=" + mName + ", place_id="
                + mPlaceId + ", vicinity=" + mVicinity + ", photo="
                + mPhotoReference + ", rating=" + mRating + ", location="
                + mLocation.latitude + "," + mLocation.longitude + "}";
    }

    public Place Clone() {
        return new Place(mLocation, mIcon, mName, mPlaceId, mVicinity, mPhotoReference, mRating, mPlaceDetails);
    }
}
