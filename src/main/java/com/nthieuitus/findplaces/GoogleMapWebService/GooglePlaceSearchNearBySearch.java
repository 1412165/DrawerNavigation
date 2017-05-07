package com.nthieuitus.findplaces.GoogleMapWebService;

import android.net.Uri;
import android.util.Log;

import com.nthieuitus.findplaces.Model.MyLocation;
import com.nthieuitus.findplaces.Model.Place;
import com.nthieuitus.findplaces.Model.PlaceList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Nguyen Trung Hieu on 06-May-17.
 */

public class GooglePlaceSearchNearBySearch extends GoogleMapWebService {
    private static final String URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private final String TAG_LOG = GooglePlaceSearchNearBySearch.class.getName();

    GooglePlaceSearchNearBySearchListener listener;
    MyLocation mLocation;
    String mNextPageToken;
    String mRankBy;
    String mKeyword;

    PlaceList mPlaceList;

    public GooglePlaceSearchNearBySearch(GooglePlaceSearchNearBySearchListener listener, MyLocation location, String keyword) {
        this.listener = listener;
        this.mLocation = location;
        this.mRankBy = "distance";
        this.mKeyword = keyword;
        this.mPlaceList = new PlaceList();
    }

    public GooglePlaceSearchNearBySearch(PlaceList PlaceList) {
        this.mNextPageToken = PlaceList.next_page_token;
        this.mPlaceList = PlaceList;
    }



    public String createURI() {
        Uri.Builder builder = Uri.parse(URL).buildUpon();
        if (mLocation != null) {
            builder.appendQueryParameter(
                    "location",
                    Double.toString(mLocation.latitude) + ","
                            + Double.toString(mLocation.longitude));
            if (mRankBy != null) {
                builder.appendQueryParameter("rankby", mRankBy);
                if (mKeyword != null)
                    builder.appendQueryParameter("keyword", mKeyword);
            }
        } else if (mNextPageToken != null) {
            builder.appendQueryParameter("pagetoken", this.mNextPageToken);
        }
        builder.appendQueryParameter("key", API_KEY);

        return builder.build().toString();
    }

    public void execute() {
        if (mLocation == null)
            return;

        listener.onGooglePlaceSearchStart();
        new GooglePlaceSearchNearBySearchDownloadJsonData().execute(createURI());
    }

    private void parseJSON(String data) {
        Log.d(TAG_LOG, "parseJSON");

        try {
            JSONObject jsonData = new JSONObject(data);

            JSONArray resultArray = jsonData.getJSONArray("results");
            for (int i = 0; i < resultArray.length(); ++i) {
                JSONObject jsonPlace = resultArray.getJSONObject(i);

                String icon = jsonPlace.getString("icon");
                String name = jsonPlace.getString("name");
                String place_id = jsonPlace.getString("place_id");
                String vicinity = jsonPlace.getString("vicinity");
                String rating = null;
                if (jsonPlace.has("rating"))
                    rating = jsonPlace.getString("rating");

                double geoLat, geoLng;
                {
                    JSONObject jsonGeometry = jsonPlace
                            .getJSONObject("geometry");
                    JSONObject jsonLocation = jsonGeometry
                            .getJSONObject("location");
                    geoLat = jsonLocation.getDouble("lat");
                    geoLng = jsonLocation.getDouble("lng");
                }
                MyLocation location = new MyLocation(geoLat, geoLng);

                String photo_reference = null;
                {
                    try {
                        JSONArray photosArray = jsonPlace
                                .getJSONArray("photos");
                        JSONObject jsonPhoto = photosArray.getJSONObject(0);
                        photo_reference = jsonPhoto
                                .getString("photo_reference");
                    } catch (Exception e) {

                    }
                }

                Place place = new Place(location, icon, name, place_id, vicinity, photo_reference, rating, null);
                this.mPlaceList.places.add(place);
            }

            try {
                // next_page_token
                String next_page_token = jsonData.getString("next_page_token");
                this.mPlaceList.next_page_token = next_page_token;

            } catch (Exception e) {
                this.mPlaceList.next_page_token = null; // End of token
            }

            for (Place place : mPlaceList.places) {
                Log.v(TAG_LOG, place.toString());
            }

        } catch (Exception e) {
            Log.d(TAG_LOG, "error", e);
        }
        Log.d(TAG_LOG, "Finish parseJSON!");
        listener.onGooglePlaceSearchSuccess(mPlaceList);
    }

    protected class GooglePlaceSearchNearBySearchDownloadJsonData extends DownloadRawData {

        @Override
        protected void onPostExecute(String result) {
            parseJSON(result);
        }
    }
}
