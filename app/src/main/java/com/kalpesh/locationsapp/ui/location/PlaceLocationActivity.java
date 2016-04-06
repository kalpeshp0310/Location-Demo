package com.kalpesh.locationsapp.ui.location;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kalpesh.locationsapp.R;
import com.kalpesh.locationsapp.model.Place;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kalpeshpatel on 06/04/16.
 */
public class PlaceLocationActivity extends AppCompatActivity implements OnMapReadyCallback, PlaceLocationPresenter.PlaceLocationView {

    private static final String KEY_PLACE = "KEY_PLACE";
    private static final int DEFAULT_ZOOM_LEVEL = 18;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private Place place;
    private GoogleMap googleMap;
    private PlaceLocationPresenter presenter;

    public static Intent getCallingIntent(Context context, Place place) {
        Intent intent = new Intent(context, PlaceLocationActivity.class);
        intent.putExtra(KEY_PLACE, place);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        if (savedInstanceState == null) {
            place = getIntent().getParcelableExtra(KEY_PLACE);
        } else {
            place = savedInstanceState.getParcelable(KEY_PLACE);
        }

        presenter = new PlaceLocationPresenter(place);
        presenter.attachView(this);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_PLACE, place);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        presenter.onMapReady();
    }

    @Override
    public void initMap() {
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);
    }

    @Override
    public void showPlaceOnMap(Place place) {
        getSupportActionBar().setTitle(place.name);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.geometry.location.lat, place.geometry.location.lng), DEFAULT_ZOOM_LEVEL));
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(place.geometry.location.lat, place.geometry.location.lng))
                .title(place.name)
                .visible(true));
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setShowLoading(boolean show) {

    }

    @Override
    public void showErrorMessage(String message) {

    }
}
