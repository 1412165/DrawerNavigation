package com.nthieuitus.findplaces.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.nthieuitus.findplaces.Architecture.PlaceInfo;
import com.nthieuitus.findplaces.Model.GlobalVars;
import com.nthieuitus.findplaces.R;
import com.nthieuitus.findplaces.Utility.StringIsEmptyOrNull;

/**
 * Created by Nguyen Trung Hieu on 06-May-17.
 */

public class OverviewPlaceInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    Context context;

    public OverviewPlaceInfoWindowAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View v = LayoutInflater.from(context).inflate(R.layout.info_window_place_overview, null);
        if (v == null)
            return null;

        PlaceInfo place = GlobalVars.markerData.get(marker);
        if (place == null)
            return null;

        TextView txtPlaceName = (TextView) v.findViewById(R.id.txtPlaceName);
        TextView txtPlaceAddress = (TextView) v.findViewById(R.id.txtPlaceAddress);
        TextView txtRating = (TextView) v.findViewById(R.id.txtRating);
        TextView txtDistance = (TextView) v.findViewById(R.id.txtDistance);

        txtPlaceName.setText(place.name);
        txtPlaceAddress.setText(place.address);
        txtDistance.setText(place.toMyLocation().distanceTo(GlobalVars.getUserLocation()));

        if (!StringIsEmptyOrNull.check(place.rating)) {
            txtRating.setVisibility(View.VISIBLE);
            txtRating.setText("Rating " + place.rating);
        } else {
            txtRating.setVisibility(View.GONE);
        }
        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
