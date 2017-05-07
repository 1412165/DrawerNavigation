package com.nthieuitus.findplaces.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nthieuitus.findplaces.Architecture.PlaceInfo;
import com.nthieuitus.findplaces.Manager.KeywordItemManager;
import com.nthieuitus.findplaces.Model.GlobalVars;
import com.nthieuitus.findplaces.Model.MyLocation;
import com.nthieuitus.findplaces.R;

import java.util.List;

/**
 * Created by Nguyen Trung Hieu on 06-May-17.
 */

public class PlacesAdapter extends ArrayAdapter<PlaceInfo>{
    Context context;
    List<PlaceInfo> places;
    int resource;

    public PlacesAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<PlaceInfo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.places = objects;
        this.resource = resource;
    }


    @Override
    public int getCount() {
        return places.size();
    }

    @Nullable
    @Override
    public PlaceInfo getItem(int position) {
        return places.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(resource, null);
        }

        PlaceInfo place = getItem(position);
        if (place != null) {
            ImageView imageViewIcon = (ImageView)v.findViewById(R.id.ivIcon);
            TextView txtPlaceName = (TextView)v.findViewById(R.id.txtPlaceName);
            TextView txtDistance = (TextView) v.findViewById(R.id.txtDistance);
            TextView txtPlaceAddress = (TextView) v.findViewById(R.id.txtPlaceAddress);

            imageViewIcon.setImageResource(KeywordItemManager.findIconByKeyword(place.keyword));
            txtPlaceName.setText(place.name);
            txtPlaceAddress.setText(place.address);

            txtDistance.setText(new MyLocation(place.lat, place.lng).distanceTo(GlobalVars.getUserLocation()));
        }
        return v;
    }

    public void updateData (List<PlaceInfo> places){
        this.places = places;
        this.notifyDataSetChanged();
    }
}
