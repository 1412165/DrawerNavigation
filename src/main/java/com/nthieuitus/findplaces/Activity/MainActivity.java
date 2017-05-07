package com.nthieuitus.findplaces.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nthieuitus.findplaces.Adapter.KeywordsAdapter;
import com.nthieuitus.findplaces.Adapter.OverviewPlaceInfoWindowAdapter;
import com.nthieuitus.findplaces.Adapter.PlacesAdapter;
import com.nthieuitus.findplaces.Architecture.GooglePlaceImporter;
import com.nthieuitus.findplaces.Architecture.PlaceInfo;
import com.nthieuitus.findplaces.GPS.GPSTracker;
import com.nthieuitus.findplaces.GPS.GPSTrackerListener;
import com.nthieuitus.findplaces.GoogleMapWebService.GooglePlaceSearchNearBySearch;
import com.nthieuitus.findplaces.GoogleMapWebService.GooglePlaceSearchNearBySearchListener;
import com.nthieuitus.findplaces.Manager.KeywordItemManager;
import com.nthieuitus.findplaces.Model.DirectionFinder;
import com.nthieuitus.findplaces.Model.DirectionFinderListener;
import com.nthieuitus.findplaces.Model.GlobalVars;
import com.nthieuitus.findplaces.Model.KeywordItem;
import com.nthieuitus.findplaces.Model.MyLocation;
import com.nthieuitus.findplaces.Model.Place;
import com.nthieuitus.findplaces.Model.PlaceList;
import com.nthieuitus.findplaces.Model.RouteFinder;
import com.nthieuitus.findplaces.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback,
        View.OnClickListener, AdapterView.OnItemClickListener,
        GooglePlaceSearchNearBySearchListener,
        GPSTrackerListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener,
        DirectionFinderListener{

    private GoogleMap mMap;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    String TAG = MainActivity.class.getName();
    Marker placeMarker;

    private OverviewPlaceInfoWindowAdapter overviewPlaceInfoWindowAdapter;
    private GPSTracker gpsTracker;

    // Marker for places
    private List<Marker> markers = new ArrayList<>();

    // Progress dialog search
    private ProgressDialog progressDialog;

    // Drawer Layout Menu
    private DrawerLayout drawerLayout;

    // Marker My Location
    private Marker userLocationMarker;

    // Left navigation Drawer
    private ListView lvKeywords;
    private KeywordsAdapter adapterKeyword;
    private ImageView btnOpenKeywordDrawer;

    //Right navigation drawer - Result Places
    private ListView lvPlaces;
    private PlacesAdapter adapterPlace;
    private ImageView btnOpenPlaceDrawer;

    // Buttons
    private ImageView btnMyLocation;
    private ImageView btnSetting;

    boolean isGoogleDone = false;

    // Google Direction Finder
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialogFinder;
    private Button btnFindPath;
    private EditText editTextOrigin;
    private EditText editTextDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        initialize();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void initialize() {
        initMap();
        initComponents();
        initGPS();

    }

    private void initComponents() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawLayout);
        lvKeywords = (ListView) findViewById(R.id.lvKeywords);
        List<KeywordItem> keywordItemList = KeywordItemManager.populateDefaultKeywordItems();

        //Keywords Navigation Drawer
        adapterKeyword = new KeywordsAdapter(this, R.layout.item_place_keyword, keywordItemList);
        lvKeywords.setAdapter(adapterKeyword);
        lvKeywords.setOnItemClickListener(this);
        btnOpenKeywordDrawer = (ImageView) findViewById(R.id.btnOpenKeywordDrawer);
        btnOpenKeywordDrawer.setOnClickListener(this);

        //Search Places Navigation Drawer
        List<PlaceInfo> places = new ArrayList<>();
        adapterPlace = new PlacesAdapter(this, R.layout.item_place_overview, places);
        lvPlaces = (ListView) findViewById(R.id.lvPlaces);
        lvPlaces.setAdapter(adapterPlace);
        lvPlaces.setOnItemClickListener(this);
        btnOpenPlaceDrawer = (ImageView) findViewById(R.id.btnOpenPlaceDrawer);
        btnOpenPlaceDrawer.setOnClickListener(this);
        btnOpenPlaceDrawer.setVisibility(View.GONE);

        //My Location Button
        btnMyLocation = (ImageView) findViewById(R.id.btnMyLocation);
        btnMyLocation.setOnClickListener(this);

        // Direction
        btnFindPath = (Button)findViewById(R.id.btn_findpath);
        btnFindPath.setOnClickListener(this);
        editTextOrigin = (EditText)findViewById(R.id.edt_start);
        editTextDestination = (EditText)findViewById(R.id.edt_end);


    }

    private void initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initGPS() {
        gpsTracker = new GPSTracker(this, this);
        if (gpsTracker.canGetLocation()) {
            double lat = gpsTracker.getLatitude();
            double lng = gpsTracker.getLongitude();
            Toast.makeText(getApplicationContext(), lat + " " + lng, Toast.LENGTH_SHORT).show();
            if (lat != 0 || lng != 0) {
                GlobalVars.location = new MyLocation(lat, lng);
            }
        } else {
            gpsTracker.showSettingsAlert();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        refreshUserGPSLocation();
        mMap.setOnInfoWindowClickListener(this);

        overviewPlaceInfoWindowAdapter = new OverviewPlaceInfoWindowAdapter(this);
        mMap.setInfoWindowAdapter(overviewPlaceInfoWindowAdapter);
        mMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOpenKeywordDrawer:
                drawerLayout.openDrawer(Gravity.LEFT);
                GlobalVars.drawer = GlobalVars.DrawerType.KEYWORDS;
                break;
            case R.id.btnOpenPlaceDrawer:
                adapterPlace.updateData(GlobalVars.currentPlaceList);
                drawerLayout.openDrawer(Gravity.RIGHT);
                GlobalVars.drawer = GlobalVars.DrawerType.SEARCH_PLACES;
                break;
            case R.id.btnMyLocation: {
                refreshUserGPSLocation();
                Toast.makeText(this, "Move camera to your location done!", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.btn_findpath:{
                sendRequest();
                break;
            }
        }

    }

    private void sendRequest() {
        String origin = editTextOrigin.getText().toString();
        String destination = editTextDestination.getText().toString();

        if (origin.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshUserGPSLocation() {
        if(userLocationMarker!=null){
            userLocationMarker.remove();
        }
        GlobalVars.IsFakeGPS = false;
        MyLocation userLocation = GlobalVars.getUserLocation();
        if (userLocation != null) {
            LatLng userLatLng = new LatLng(userLocation.latitude, userLocation.longitude);
            userLocationMarker = mMap.addMarker(new MarkerOptions().position(userLatLng).title("Your location").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mylocation)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMap != null)
            refreshUserGPSLocation();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        drawerLayout.closeDrawers();
        if (GlobalVars.drawer == GlobalVars.DrawerType.KEYWORDS) {
            KeywordItem item = GlobalVars.keywordItem = adapterKeyword.getItem(position);

            //Reset all
            progressDialog = ProgressDialog.show(this, "", "Finding " + GlobalVars.keywordItem.text + "...");
            for (Marker maker : markers) {
                maker.remove();
            }

            //Reset common place list and request for new places
            isGoogleDone = false;

            GlobalVars.currentPlaceList = new ArrayList<>();
            new GooglePlaceSearchNearBySearch((GooglePlaceSearchNearBySearchListener) this, GlobalVars.getUserLocation(), item.text).execute();

        }
        else if (GlobalVars.drawer == GlobalVars.DrawerType.SEARCH_PLACES){
            PlaceInfo place = adapterPlace.getItem(position);
            GlobalVars.currentPlace = place;
            Intent intent = new Intent(MainActivity.this, PlaceDetailActivity.class);
            startActivity(intent);
        } /*else if (GlobalVars.drawer == GlobalVars.DrawerType.FAVORITE_PLACES) {
//            PlaceInfo place = FavoritePlacesManager.getInstance().places.get(position);
//            GlobalVars.currentPlace = place;
//            Intent intent = new Intent(MainActivity.this, PlaceDetailActivity.class);
//            startActivity(intent);
        }*/

    }

    @Override
    public void onGooglePlaceSearchStart() {

    }

    @Override
    public void onGooglePlaceSearchSuccess(PlaceList placeList) {
        isGoogleDone = true;

        List<Place> places = placeList.places;

        GooglePlaceImporter googlePlaceImporter = new GooglePlaceImporter();
        //Display on Google maps
        for (int i = 0; i < places.size(); ++i) {
            PlaceInfo place = googlePlaceImporter.Convert(places.get(i));
            GlobalVars.currentPlaceList.add(place);

            LatLng position = new LatLng(place.lat, place.lng);
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(position)
                    .icon(BitmapDescriptorFactory.fromResource(GlobalVars.keywordItem.icon)));

            GlobalVars.markerData.put(marker, place);
            markers.add(marker);
        }

        LatLng centerPosition = getCenterLocation(places);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerPosition, 15));

        //Display on list view
        adapterPlace.updateData(GlobalVars.currentPlaceList);
        btnOpenPlaceDrawer.setVisibility(View.VISIBLE);

        //Done
        if (isGoogleDone)
            progressDialog.dismiss();
    }

    private LatLng getCenterLocation(List<Place> places) {
        if (GlobalVars.getUserLocation() == null)
            return null;

        LatLng ans = new LatLng(GlobalVars.getUserLocation().latitude, GlobalVars.getUserLocation().longitude);
        return ans;
    }

    @Override
    public void onGPSTrackerLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        //Toast.makeText(this, "TrackerLocationChanged: " + lat + "," + lng, Toast.LENGTH_SHORT).show();
        if (lat != 0 || lng != 0) {
            GlobalVars.location = new MyLocation(lat, lng);
            if (mMap != null)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));

            refreshUserGPSLocation();
        }
    }

    @Override
    public void onGPSTrackerStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(this, "Status changed: " + status, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        PlaceInfo place = GlobalVars.markerData.get(marker);
        GlobalVars.currentPlace = place;
        Intent intent = new Intent(MainActivity.this, PlaceDetailActivity.class);
        startActivity(intent);

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        GlobalVars.fakeLocation = new MyLocation(latLng.latitude, latLng.longitude);
        GlobalVars.IsFakeGPS = true;
        showCurrentTapLocation();

    }

    private void showCurrentTapLocation() {
        if (userLocationMarker != null)
            userLocationMarker.remove();

        MyLocation userLocation = GlobalVars.getUserLocation();
        if (userLocation != null) {
            LatLng userLatLng = new LatLng(userLocation.latitude, userLocation.longitude);
            userLocationMarker = mMap.addMarker(new MarkerOptions().position(userLatLng).title("Your location").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mylocation)));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onDirectionFinderStart() {

        progressDialogFinder = ProgressDialog.show(this, "Please wait", "Finding direction...", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<RouteFinder> routes) {

        progressDialogFinder.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (RouteFinder route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView)findViewById(R.id.tv_dis)).setText(route.distance.text);
            ((TextView)findViewById(R.id.tv_time)).setText(route.duration.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));

            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions()
                    .geodesic(true)
                    .color(Color.BLUE)
                    .width(10);

            for (int i = 0; i < route.points.size(); i++) {
                polylineOptions.add(route.points.get(i));
            }

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }
}
