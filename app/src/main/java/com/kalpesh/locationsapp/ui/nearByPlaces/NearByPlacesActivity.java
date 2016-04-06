package com.kalpesh.locationsapp.ui.nearByPlaces;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kalpesh.locationsapp.R;
import com.kalpesh.locationsapp.model.Place;
import com.kalpesh.locationsapp.navigation.Navigator;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kalpeshpatel on 06/04/16.
 */
public class NearByPlacesActivity extends AppCompatActivity implements NearByPlacesPresenter.NearByPlacesView, PlacesListAdapter.ClickListener {

    private static final String KEY_LATITUDE = "KEY_LATITUDE";
    private static final String KEY_LONGITUDE = "KEY_LONGITUDE";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycler_view)
    RecyclerView placesRecyclerView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    private NearByPlacesPresenter presenter;
    private PlacesListAdapter placesListAdapter;
    private Navigator navigator;

    private double latitude;
    private double longitude;


    public static Intent getCallingIntent(Context context, double lat, double lng) {
        Intent intent = new Intent(context, NearByPlacesActivity.class);
        intent.putExtra(KEY_LATITUDE, lat);
        intent.putExtra(KEY_LONGITUDE, lng);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_places);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.nearby_places);

        if (savedInstanceState == null) {
            latitude = getIntent().getDoubleExtra(KEY_LATITUDE, 0);
            longitude = getIntent().getDoubleExtra(KEY_LONGITUDE, 0);
        } else {
            latitude = savedInstanceState.getDouble(KEY_LATITUDE);
            longitude = savedInstanceState.getDouble(KEY_LONGITUDE);
        }

        navigator = new Navigator();

        placesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        placesRecyclerView.setHasFixedSize(true);

        presenter = new NearByPlacesPresenter(latitude, longitude);
        presenter.attachView(this);
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
        outState.putDouble(KEY_LATITUDE, latitude);
        outState.putDouble(KEY_LONGITUDE, longitude);
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
    public void showNearByPlaces(List<Place> places) {
        if (placesListAdapter == null) {
            placesListAdapter = new PlacesListAdapter(this, this);
            placesRecyclerView.setAdapter(placesListAdapter);
        }
        placesListAdapter.updatePlaces(places);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setShowLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(NearByPlacesActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPlaceClicked(Place place) {
        presenter.onPlaceSelected(place);
    }

    @Override
    public void showPlaceDetail(Place place) {
        navigator.navigateToPlaceDetailScreen(this, place);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
