package com.nthieuitus.findplaces.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nthieuitus.findplaces.Architecture.PlaceInfo;
import com.nthieuitus.findplaces.GoogleMapWebService.GoogleMapsDirections;
import com.nthieuitus.findplaces.GoogleMapWebService.GooglePlaceDetailsSimple;
import com.nthieuitus.findplaces.GoogleMapWebService.GooglePlaceDetailsSimpleListener;
import com.nthieuitus.findplaces.Model.GlobalVars;
import com.nthieuitus.findplaces.Model.MyLocation;
import com.nthieuitus.findplaces.R;
import com.nthieuitus.findplaces.Utility.StringIsEmptyOrNull;

import java.util.ArrayList;
import java.util.List;

public class PlaceDetailActivity extends AppCompatActivity implements
        View.OnClickListener, GooglePlaceDetailsSimpleListener {
    final String TAG_LOG = this.getClass().getSimpleName();
    private static final int TAKE_PICTURE_AND_UPLOAD = 2000;
    ImageView btnBack;
    ImageView btnFavorite;
    ImageView btnShare;
    ImageView btnDriving;
    ImageView btnCycling;
    ImageView btnWalking;
    ImageView btnAddToCalender;

    TextView txtDistance;
    TextView txtPlaceName;
    TextView txtPlaceAddress;
    TextView txtContact;
    TextView txtInternationalContact;
    TextView txtWebsite;

    RelativeLayout frameContact;
    RelativeLayout frameWebsite;
    HorizontalScrollView frameImageGallery;
    PlaceInfo place;

    ProgressBar progressBarLoading;
    ListView listViewNewsFeed;

    //ImageGalleryAdapter adapterImageGallery;
    //FBFeedItemAdapter adapterNewsFeed;

    //Floating Action Buttons
    //FloatingActionButton fabUploadPhoto;
    //FloatingActionButton fabPostYourStatus;
    //FloatingActionButton fabAddToCalendar;

    //FloatingActionButton fabFilterByLike;
    //FloatingActionButton fabFilterByTimeLine;

    //FeedItemsManager feedItemsManager = new FeedItemsManager(new ArrayList<FBFeedItem>());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        init();
    }

    private void init() {
        initComponents();
        place = GlobalVars.currentPlace;
        displayPlaceInfo(place);

        if (place != null && !StringIsEmptyOrNull.check(place.googlePlaceId)) {
            new GooglePlaceDetailsSimple(this, place).execute();
        }

    }

    private void initComponents() {
        frameContact = (RelativeLayout) findViewById(R.id.frameContact);
        frameWebsite = (RelativeLayout) findViewById(R.id.frameWebsite);
        frameImageGallery = (HorizontalScrollView) findViewById(R.id.frameImageGallery);
        frameContact.setVisibility(View.GONE);
        frameWebsite.setVisibility(View.GONE);
        frameImageGallery.setVisibility(View.GONE);

        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnFavorite = (ImageView) findViewById(R.id.btnFavorite);
        btnShare = (ImageView) findViewById(R.id.btnShare);
        btnCycling = (ImageView) findViewById(R.id.btnCycling);
        btnDriving = (ImageView) findViewById(R.id.btnDriving);
        btnWalking = (ImageView) findViewById(R.id.btnWalking);

        txtDistance = (TextView) findViewById(R.id.txtDistance);
        txtPlaceName = (TextView) findViewById(R.id.txtPlaceName);
        txtPlaceAddress = (TextView) findViewById(R.id.txtPlaceAddress);
        txtWebsite = (TextView) findViewById(R.id.txtWebsite);
        txtContact = (TextView) findViewById(R.id.txtContact);
        txtInternationalContact = (TextView) findViewById(R.id.txtInternationalContact);

        btnBack.setOnClickListener(this);
        btnFavorite.setOnClickListener(this);
        btnShare.setOnClickListener(this);

        btnCycling.setOnClickListener(this);
        btnDriving.setOnClickListener(this);
        btnWalking.setOnClickListener(this);

        txtWebsite.setOnClickListener(this);
        txtContact.setOnClickListener(this);
        txtInternationalContact.setOnClickListener(this);

        progressBarLoading = (ProgressBar) findViewById(R.id.progressBarLoading);
        progressBarLoading.setVisibility(View.GONE);

        //Photos
        LinearLayout linearLayoutImageGallery = (LinearLayout) findViewById(R.id.llImageGallery);
        List<String> images = new ArrayList<>();
        //adapterImageGallery = new ImageGalleryAdapter(this, linearLayoutImageGallery, images);
    }

    private void displayPlaceInfo(PlaceInfo place) {
        if (place == null)
            return;

        displayBasicPlaceInfo(place);

        //Place Address
        txtPlaceAddress.setText(place.address);
        //Website
        if (place.website != null && !place.website.isEmpty()) {
            frameWebsite.setVisibility(View.VISIBLE);
            txtWebsite.setText(place.website);
        }

        //Contact
        if (place.phoneNumber != null && !place.phoneNumber.isEmpty() || place.internationalPhoneNumber != null && !place.internationalPhoneNumber.isEmpty()) {
            frameContact.setVisibility(View.VISIBLE);
            txtContact.setText(place.phoneNumber);
            txtInternationalContact.setText(place.internationalPhoneNumber);
        }

    }

    private void displayBasicPlaceInfo(PlaceInfo place) {
        if (place == null)
            return;

        txtPlaceName.setText(place.name);
        txtPlaceAddress.setText(place.address);
        txtDistance.setText(new MyLocation(place.lat, place.lng).distanceTo(GlobalVars.getUserLocation()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                this.finish();
                break;
            //case R.id.btnFavorite:
            //    addToFavorite();
            //    break;
            case R.id.btnShare:
                shareThisPlace();
                break;
            case R.id.btnCycling:
                onCycling();
                break;
            case R.id.btnDriving:
                onDriving();
                break;
            case R.id.btnWalking:
                onWalking();
                break;
            case R.id.txtWebsite:
                onWebsite();
                break;
            case R.id.txtContact:
                onContact();
                break;
            case R.id.txtInternationalContact:
                onInternationalContact();
                break;
        }
    }

    private void onInternationalContact() {
        String phoneNumber = txtInternationalContact.getText().toString();
        phoneNumber = phoneNumber.replace(" ", "");
        Uri number = Uri.parse("tel: " + phoneNumber);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
    }

    private void onContact() {
        String phoneNumber = txtContact.getText().toString();
        phoneNumber = phoneNumber.replace(" ", "");
        Uri number = Uri.parse("tel: " + phoneNumber);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
    }

    private void onWebsite() {
        String url = txtWebsite.getText().toString();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void onWalking() {
        Intent intent = new Intent(this, PlaceDirectionActivity.class);
        intent.putExtra("mode", GoogleMapsDirections.TRAVEL_MODE_WALKING);
        Toast.makeText(this, "DI BO!", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    private void onDriving() {
        Intent intent = new Intent(this, PlaceDirectionActivity.class);
        intent.putExtra("mode", GoogleMapsDirections.TRAVEL_MODE_DRIVING);
        Toast.makeText(this, "XE OTO!", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    private void onCycling() {
        Intent intent = new Intent(this, PlaceDirectionActivity.class);
        intent.putExtra("mode", GoogleMapsDirections.TRAVEL_MODE_BICYCLING);
        Toast.makeText(this, "XE DAP!", Toast.LENGTH_SHORT).show();

        startActivity(intent);
    }

    private void shareThisPlace() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareTitle = "PlacesNearMe - Sharing a place";
        String shareBody = place.name + "\r\n" +
                place.address + "\r\n" +
                String.format("http://maps.google.com/maps?q=loc:%f,%f8&z=20", place.lat, place.lng);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareTitle);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    public void onGooglePlaceDetailsSimpleStart() {

    }

    @Override
    public void onGooglePlaceDetailsSimpleFailed() {

    }

    @Override
    public void onGooglePlaceDetailsSimpleSuccess(PlaceInfo place) {
        if (place == null)
            return;

        displayPlaceInfo(place);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
