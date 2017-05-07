package com.nthieuitus.findplaces.GoogleMapWebService;

import android.net.Uri;
import android.util.Log;

import com.nthieuitus.findplaces.Model.Photo;
import com.nthieuitus.findplaces.Model.Place;
import com.nthieuitus.findplaces.Model.PlaceDetails;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nguyen Trung Hieu on 06-May-17.
 */

public class GooglePlaceDetails extends GoogleMapWebService {
    private static final String URL = "https://maps.googleapis.com/maps/api/place/details/json?";
    private final String TAG_LOG = GooglePlaceDetails.class.getName();
    private final GooglePlaceDetailsListener listener;

    Place mPlace;

    public GooglePlaceDetails(GooglePlaceDetailsListener listener, Place place) {
        this.listener = listener;
        this.mPlace = place;
    }

    public void execute() {
        listener.onGooglePlaceDetailsStart();
        new GooglePlaceDetailsDownloadJsonData().execute(createURL());
    }

    private void parseJSON(String data) {
        Log.d(TAG_LOG, "parseJSON");

        try {
            PlaceDetails placeDetails = new PlaceDetails();

            JSONObject jsonData = new JSONObject(data);
            JSONObject resultObject = jsonData.getJSONObject("result");

            String formatted_address = null;
            if (resultObject.has("formatted_address"))
                formatted_address = resultObject.getString("formatted_address");

            String formatted_phone_number = null;
            if (resultObject.has("formatted_phone_number"))
                formatted_phone_number = resultObject.getString("formatted_phone_number");

            String international_phone_number = null;
            if (resultObject.has("international_phone_number"))
                international_phone_number = resultObject.getString("international_phone_number");

            String website = null;
            if (resultObject.has("website"))
                website = resultObject.getString("website");

            placeDetails.formattedAddress = formatted_address;
            placeDetails.localPhoneNumber = formatted_phone_number;
            placeDetails.internationalPhoneNumber = international_phone_number;
            placeDetails.website = website;

            //Parse Photos
            List<Photo> photosList = new ArrayList<Photo>();
            try {
                JSONArray photosArray = resultObject.getJSONArray("photos");
                for (int i = 0; i < photosArray.length(); ++i) {
                    JSONObject photoObject = photosArray.getJSONObject(i);
                    String photoReference = photoObject
                            .getString("photo_reference");
                    int width = photoObject.getInt("width");
                    int height = photoObject.getInt("height");
                    Photo photo = new Photo(photoReference, width, height);
                    photosList.add(photo);
                }

            } catch (Exception e) {
                Log.d(TAG_LOG, "No photos");
            }
            placeDetails.photos = photosList;

            //Parse Reviews
            /*List<Review> reviewsList = new ArrayList<Review>();
            try {
                JSONArray reviewsArray = resultObject.getJSONArray("reviews");
                for (int i = 0; i < reviewsArray.length(); ++i) {
                    JSONObject reviewObject = reviewsArray.getJSONObject(i);
                    String author_name = reviewObject.getString("author_name");
                    String author_url = null;
                    if (reviewObject.has("author_url")) {
                        author_url = reviewObject.getString("author_url");
                    }
                    String profile_photo_url = null;
                    if (reviewObject.has("profile_photo_url")) {
                        profile_photo_url = reviewObject.getString("profile_photo_url");
                        profile_photo_url = "http:" + profile_photo_url;
                    }
                    double rating = reviewObject.getDouble("rating");
                    String text = reviewObject.getString("text");
                    long time = reviewObject.getLong("time");
                    Review review = new Review(author_name, author_url, profile_photo_url, rating,
                            text, time);
                    reviewsList.add(review);
                }
            } catch (Exception e) {
                Log.d(TAG_LOG, "No reviews");
            }
            placeDetails.reviews = reviewsList;*/

            mPlace.mPlaceDetails = placeDetails;

        } catch (Exception e) {
            Log.e(TAG_LOG, "error", e);
        }
        Log.d(TAG_LOG, "Finish parseJSON successfully!");
        listener.onGooglePlaceDetailsSuccess(mPlace);
    }

    public String createURL() {
        Uri.Builder builder = Uri.parse(URL).buildUpon();
        builder.appendQueryParameter("placeid", this.mPlace.mPlaceId);
        builder.appendQueryParameter("key", API_KEY);

        return builder.build().toString();
    }

    protected class GooglePlaceDetailsDownloadJsonData extends DownloadRawData {

        @Override
        protected void onPostExecute(String result) {
            parseJSON(result);
        }
    }
}
