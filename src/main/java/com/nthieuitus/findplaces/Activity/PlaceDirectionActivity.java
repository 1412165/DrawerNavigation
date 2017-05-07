package com.nthieuitus.findplaces.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nthieuitus.findplaces.Adapter.DirectionRouteAdapter;
import com.nthieuitus.findplaces.GoogleMapWebService.GoogleMapsDirections;
import com.nthieuitus.findplaces.GoogleMapWebService.GoogleMapsDirectionsListener;
import com.nthieuitus.findplaces.Model.GlobalVars;
import com.nthieuitus.findplaces.Model.Route;
import com.nthieuitus.findplaces.Model.Step;
import com.nthieuitus.findplaces.R;

import java.util.ArrayList;
import java.util.List;

public class PlaceDirectionActivity extends FragmentActivity implements OnMapReadyCallback,
        AdapterView.OnItemClickListener,
        GoogleMapsDirectionsListener,
        View.OnClickListener{

    private GoogleMap mMap;
    private DirectionRouteAdapter routeAdapter;
    private ListView listViewDirection;
    private ImageView iconDriving;
    private ImageView btnBack;
    private TextView txtCurrentLocationToDestination;
    private List<Polyline> polylines = new ArrayList<>();
    private List<Marker> markers = new ArrayList<>();
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_direction);

        initialize();
    }

    private void initialize() {
        initComponents();
        initMap();
        initAdapter();

        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        changeStyleByMode();

        //Get direction -> Tim duong di theo toa do -> Thay vi placeID
        new GoogleMapsDirections(this, GlobalVars.getUserLocation(), GlobalVars.currentPlace.toMyLocation(), mode)
                .execute();
    }

    private void changeStyleByMode() {
        switch (mode) {
            case GoogleMapsDirections.TRAVEL_MODE_BICYCLING:
                iconDriving.setImageResource(R.drawable.ic_cycling_pressed);
                break;
            case GoogleMapsDirections.TRAVEL_MODE_DRIVING:
                iconDriving.setImageResource(R.drawable.ic_driving_pressed);
                break;
            case GoogleMapsDirections.TRAVEL_MODE_WALKING:
                iconDriving.setImageResource(R.drawable.ic_walking_pressed);
                break;
        }
    }

    private void initComponents() {
        listViewDirection = (ListView) findViewById(R.id.listViewDirection);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        txtCurrentLocationToDestination = (TextView) findViewById(R.id.txtPlaceName);
        iconDriving = (ImageView) findViewById(R.id.iconDriving);

        btnBack.setOnClickListener(this);
    }

    private void initAdapter() {
        List<Route> routes = new ArrayList<Route>();
        routeAdapter = new DirectionRouteAdapter(this, R.layout.item_place_direction_route, routes);
        listViewDirection.setAdapter(routeAdapter);
        listViewDirection.setOnItemClickListener(this);
    }

    private void initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        displayDirectionOnMap(routeAdapter.getRoutes(), position);
    }

    @Override
    public void onGoogleMapsDirectionsStart() {

    }

    @Override
    public void onGoogleMapsDirectionsSuccess(List<Route> routes) {
        routeAdapter.setData(routes);
        routeAdapter.notifyDataSetChanged();

        displayDirectionOnMap(routes, 0);
    }


    private void displayDirectionOnMap(List<Route> routes, int idxRoute) {
        removeOldStuff();
        txtCurrentLocationToDestination.setText("Current Location to " + GlobalVars.currentPlace.name);

        if (routes == null || routes.size() == 0) {
            markers.add(mMap.addMarker(new MarkerOptions().position(GlobalVars.getUserLocation().toLatLng()).title("My current location")));
            markers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(GlobalVars.currentPlace.lat, GlobalVars.currentPlace.lng)).title(GlobalVars.currentPlace.name)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(GlobalVars.currentPlace.lat, GlobalVars.currentPlace.lng), 15));
            return;
        }

        //Draw other routes
        for (int i = 0; i < routes.size(); ++i) {
            if (i != idxRoute) {
                Route route = routes.get(i);
                List<Step> steps = route.legs.get(0).steps;

                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.width(10);
                polylineOptions.color(Color.GRAY);

                for (Step step : steps) {
                    polylineOptions.add(step.startLocation.toLatLng(), step.endLocation.toLatLng());
                }
                polylines.add(mMap.addPolyline(polylineOptions));
            }
        }
        //Draw idxRoute
        {
            Route route = routes.get(idxRoute);
            List<Step> steps = route.legs.get(0).steps;

            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.width(10);
            polylineOptions.color(Color.BLUE);

            for (Step step : steps) {
                polylineOptions.add(step.startLocation.toLatLng(), step.endLocation.toLatLng());
            }
            polylines.add(mMap.addPolyline(polylineOptions));
        }

        LatLng startLocation = routes.get(idxRoute).legs.get(0).startLocation.toLatLng();
        LatLng endLocation = routes.get(idxRoute).legs.get(0).endLocation.toLatLng();
        String startAddress = routes.get(idxRoute).legs.get(0).startAddress;
        String endAddress = routes.get(idxRoute).legs.get(0).endAddress;

        markers.add(mMap.addMarker(new MarkerOptions().position(startLocation).title(startAddress)));
        markers.add(mMap.addMarker(new MarkerOptions().position(endLocation).title(endAddress)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GlobalVars.currentPlace.toLatLng(), 15));
    }

    private void removeOldStuff() {
        for (Polyline polyline : polylines) {
            polyline.remove();
        }
        polylines.clear();

        for (Marker marker : markers) {
            marker.remove();
        }
        markers.clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                this.finish();
                break;
        }
    }
}
