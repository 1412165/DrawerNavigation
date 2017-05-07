package com.nthieuitus.findplaces.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nthieuitus.findplaces.Model.Route;
import com.nthieuitus.findplaces.R;

import java.util.List;

/**
 * Created by Nguyen Trung Hieu on 06-May-17.
 */

public class DirectionRouteAdapter extends ArrayAdapter<Route> {
    Context context;
    List<Route> routes;
    int resource;

    public DirectionRouteAdapter(Context context, int resource, List<Route> routes) {
        super(context, resource, routes);
        this.context = context;
        this.routes = routes;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(resource, null);
        }

        Route item = getItem(position);
        if (item != null) {
            TextView txtRouteName = (TextView) v.findViewById(R.id.txtRouteName);
            TextView txtRouteInfo = (TextView) v.findViewById(R.id.txtRouteInfo);
            txtRouteName.setText("Via " + item.summary);
            txtRouteInfo.setText(item.legs.get(0).distance.text + " " + item.legs.get(0).duration.text);
        }

        return v;
    }

    public void setData(List<Route> routes) {
        this.routes = routes;
    }
    @Override
    public Route getItem(int position) {
        return routes.get(position);
    }

    @Override
    public int getCount() {
        return routes.size();
    }

    public List<Route> getRoutes() {
        return routes;
    }
}
