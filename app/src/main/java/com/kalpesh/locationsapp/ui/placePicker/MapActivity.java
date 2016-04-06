package com.kalpesh.locationsapp.ui.placePicker;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kalpesh.locationsapp.R;
import com.kalpesh.locationsapp.model.Location;
import com.kalpesh.locationsapp.navigation.Navigator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, MapPresenter.MapView {

    public static final int DEFAULT_ZOOM_LEVEL = 15;
    private static final int REQUEST_CHECK_SETTINGS = 1001;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private MapPresenter mapPresenter;
    private GoogleMap googleMap;
    private Marker currentLocationMarker;
    private Navigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.app_name);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapPresenter = new MapPresenter();
        navigator = new Navigator();
        mapPresenter.attachView(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                //Refrence: https://developers.google.com/android/reference/com/google/android/gms/location/SettingsApi
                switch (resultCode) {
                    case RESULT_OK:
                        // All required changes were successfully made
                        mapPresenter.onLocationSettingEnabled();
                        break;
                    case RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        mapPresenter.onLocationSettingCanceled();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapPresenter.detachView();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        mapPresenter.onMapReady();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mapPresenter.onLocationSelected(latLng);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void moveToLocation(Location location) {
        if (currentLocationMarker != null)
            currentLocationMarker.remove();

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.lat, location.lng), DEFAULT_ZOOM_LEVEL));
        currentLocationMarker = googleMap.addMarker(new MarkerOptions().position(new LatLng(location.lat, location.lng)).visible(true));
    }

    @Override
    public void initMap() {
        googleMap.setOnMapClickListener(this);
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
    }

    @Override
    public void requestEnableLocationSetting(Status status) {
        try {
            status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
        } catch (IntentSender.SendIntentException e) {
            //Error opening settings activity.
            mapPresenter.onLocationSettingsError();
        }
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNearbyPlaces(double lat, double lng) {
        navigator.navigateToPlacesList(this, lat, lng);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void setShowLoading(boolean show) {

    }
}
