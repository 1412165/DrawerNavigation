package com.nthieuitus.findplaces.Model;

/**
 * Created by Nguyen Trung Hieu on 06-May-17.
 */

public class Photo {
    String mPhotoReference;
    int mWidthMax;
    int mHeightMax;

    public Photo(String mPhotoReference, int mWidthMax, int mHeightMax) {
        this.mPhotoReference = mPhotoReference;
        this.mWidthMax = mWidthMax;
        this.mHeightMax = mHeightMax;
    }
}
