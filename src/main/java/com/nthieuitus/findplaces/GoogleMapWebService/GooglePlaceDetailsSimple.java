package com.nthieuitus.findplaces.GoogleMapWebService;

import android.net.Uri;
import android.util.Log;

import com.nthieuitus.findplaces.Architecture.PlaceInfo;
import com.nthieuitus.findplaces.Model.PlaceDetails;

import org.json.JSONObject;

/**
 * Created by Nguyen Trung Hieu on 06-May-17.
 */

public class GooglePlaceDetailsSimple extends GoogleMapWebService {
    private static final String URL = "https://maps.googleapis.com/maps/api/place/details/json?";
    private final String TAG_LOG = GooglePlaceDetails.class.getName();
    private final GooglePlaceDetailsSimpleListener listener;

    PlaceInfo mPlace;

    public GooglePlaceDetailsSimple(GooglePlaceDetailsSimpleListener listener, PlaceInfo place) {
        this.listener = listener;
        this.mPlace = place;
    }

    public void execute() {
        listener.onGooglePlaceDetailsSimpleStart();
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


            mPlace.address=formatted_address;
            mPlace.phoneNumber = formatted_phone_number;
            mPlace.internationalPhoneNumber = international_phone_number;
            mPlace.website = website;

        } catch (Exception e) {
            e.printStackTrace();
        }
        listener.onGooglePlaceDetailsSimpleSuccess(mPlace);
    }

    public String createURL() {
        Uri.Builder builder = Uri.parse(URL).buildUpon();
        builder.appendQueryParameter("placeid", this.mPlace.googlePlaceId);
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
