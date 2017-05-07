package com.nthieuitus.findplaces.Model;

import java.util.List;

/**
 * Created by Nguyen Trung Hieu on 06-May-17.
 */

public class PlaceDetails {
    public String localPhoneNumber;
    public String internationalPhoneNumber;
    public String formattedAddress;
    public String website;

    public List<Review> reviews;
    public List<Photo> photos;
}
