package com.nthieuitus.findplaces.Model;

/**
 * Created by Nguyen Trung Hieu on 06-May-17.
 */

class Review {
    public String authorName;
    public String authorUrl;
    public String profilePhotoUrl;
    public double rating;
    public String text;
    public long time;

    public Review(String authorName, String authorUrl, String profilePhotoUrl, double rating, String text, long time) {
        this.authorName = authorName;
        this.authorUrl = authorUrl;
        this.profilePhotoUrl = profilePhotoUrl;
        this.rating = rating;
        this.text = text;
        this.time = time;
    }
}

