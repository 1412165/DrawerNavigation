package com.nthieuitus.findplaces.GoogleMapWebService;

import android.net.Uri;
import android.util.Log;

import com.nthieuitus.findplaces.Model.Distance;
import com.nthieuitus.findplaces.Model.Duration;
import com.nthieuitus.findplaces.Model.Leg;
import com.nthieuitus.findplaces.Model.MyLocation;
import com.nthieuitus.findplaces.Model.Route;
import com.nthieuitus.findplaces.Model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nguyen Trung Hieu on 06-May-17.
 */

public class GoogleMapsDirections extends GoogleMapWebService{
    public static final String TRAVEL_MODE_DRIVING = "driving";
    public static final String TRAVEL_MODE_WALKING = "walking";
    public static final String TRAVEL_MODE_BICYCLING = "transit";

    GoogleMapsDirectionsListener listener;
    static final String URL = "https://maps.googleapis.com/maps/api/directions/json?";
    final String TAG_LOG = GoogleMapsDirections.class.getName();
    MyLocation originLocation;
    MyLocation destinationLocation;
    String travelMode;

    //Tạm thời là vậy, hạ hồi phân giải sau
    public GoogleMapsDirections(GoogleMapsDirectionsListener listener, MyLocation originLocation, MyLocation destinationLocation, String travelMode) {
        this.listener = listener;
        this.originLocation = originLocation;
        this.destinationLocation = destinationLocation;
        this.travelMode = travelMode;
    }

    public void execute() {
        listener.onGoogleMapsDirectionsStart();
        new GoogleMapsDirectionsDownloadJsonData().execute(createURL());
    }

    private String createURL() {
        Uri.Builder builder = Uri.parse(URL).buildUpon();
        String origin = "";
        String destination = "";
        if (originLocation != null) {
            origin = originLocation.latitude + "," + originLocation.longitude;
        }
        if (destinationLocation != null) {
            destination = destinationLocation.latitude + "," + destinationLocation.longitude;
        }

        builder.appendQueryParameter("origin", origin);
        builder.appendQueryParameter("destination", destination);
        if (travelMode != null)
            builder.appendQueryParameter("mode", travelMode);
        builder.appendQueryParameter("key", API_KEY);
        builder.appendQueryParameter("alternatives", "true");

        return builder.build().toString();
    }

    private void parseJSON(String data) {
        if (data == null)
            return;

        Log.d(TAG_LOG, "parseJSON");

        List<Route> routes = new ArrayList<Route>();
        try {
            JSONObject jsonData = new JSONObject(data);
            JSONArray jsonRoutes = jsonData.getJSONArray("routes");

            for (int i = 0; i < jsonRoutes.length(); i++) {
                Route route = new Route();

                JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
                route.copyrights = jsonRoute.getString("copyrights");
                route.overview_polyline = jsonRoute.getJSONObject("overview_polyline").getString("points");
                route.summary = jsonRoute.getString("summary");
                route.legs = new ArrayList<>();

                JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
                for (int j = 0; j < jsonLegs.length(); ++j) {
                    Leg leg = new Leg();

                    JSONObject jsonLeg = jsonLegs.getJSONObject(j);
                    JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
                    JSONObject jsonDuration = jsonLeg.getJSONObject("duration");
                    JSONObject jsonEndLocation = jsonLeg.getJSONObject("end_location");
                    JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");
                    leg.distance = new Distance(jsonDistance.getString("text"), jsonDistance.getInt("value"));
                    leg.duration = new Duration(jsonDuration.getString("text"), jsonDuration.getInt("value"));
                    leg.endAddress = jsonLeg.getString("end_address");
                    leg.startAddress = jsonLeg.getString("start_address");
                    leg.endLocation = new MyLocation(jsonEndLocation.getDouble("lat"), jsonEndLocation.getDouble("lng"));
                    leg.startLocation = new MyLocation(jsonStartLocation.getDouble("lat"), jsonStartLocation.getDouble("lng"));
                    leg.steps = new ArrayList<>();

                    JSONArray jsonSteps = jsonLeg.getJSONArray("steps");
                    for (int k = 0; k < jsonSteps.length(); ++k) {
                        Step step = new Step();

                        JSONObject jsonStep = jsonSteps.getJSONObject(k);
                        JSONObject jsonStepDistance = jsonStep.getJSONObject("distance");
                        JSONObject jsonStepDuration = jsonStep.getJSONObject("duration");
                        JSONObject jsonStepEndLocation = jsonStep.getJSONObject("end_location");
                        JSONObject jsonStepStartLocation = jsonStep.getJSONObject("start_location");
                        step.distance = new Distance(jsonStepDistance.getString("text"), jsonStepDistance.getInt("value"));
                        step.duration = new Duration(jsonStepDuration.getString("text"), jsonStepDuration.getInt("value"));
                        step.endLocation = new MyLocation(jsonStepEndLocation.getDouble("lat"), jsonStepEndLocation.getDouble("lng"));
                        step.startLocation = new MyLocation(jsonStepStartLocation.getDouble("lat"), jsonStepStartLocation.getDouble("lng"));
                        step.htmlInstruction = jsonStep.getString("html_instructions");
                        step.polyline = jsonStep.getJSONObject("polyline").getString("points");
                        leg.steps.add(step);
                    }
                    route.legs.add(leg);
                }

                routes.add(route);
            }

        } catch (JSONException e)  {

        }

        Log.d(TAG_LOG, "parseJSON Done!");
        listener.onGoogleMapsDirectionsSuccess(routes);
    }

    protected class GoogleMapsDirectionsDownloadJsonData extends DownloadRawData {

        @Override
        protected void onPostExecute(String result) {
            parseJSON(result);
        }
    }

}
