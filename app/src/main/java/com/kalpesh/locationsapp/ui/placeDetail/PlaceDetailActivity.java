package com.kalpesh.locationsapp.ui.placeDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kalpesh.locationsapp.R;
import com.kalpesh.locationsapp.model.Place;
import com.kalpesh.locationsapp.navigation.Navigator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kalpeshpatel on 06/04/16.
 */
public class PlaceDetailActivity extends AppCompatActivity implements PlaceDetailPresenter.PlaceDetailView {

    private static final String KEY_PLACE = "KEY_PLACE";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.image)
    ImageView placeImageView;
    @Bind(R.id.address)
    TextView addressTv;
    @Bind(R.id.description)
    TextView descriptionTv;
    @Bind(R.id.btn_map)
    FloatingActionButton mapBtn;


    private Place place;
    private PlaceDetailPresenter presenter;
    private Navigator navigator;


    public static Intent getCallingIntent(Context context, Place place) {
        Intent intent = new Intent(context, PlaceDetailActivity.class);
        intent.putExtra(KEY_PLACE, place);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        if (savedInstanceState == null)
            place = getIntent().getParcelableExtra(KEY_PLACE);
        else
            place = savedInstanceState.getParcelable(KEY_PLACE);

        navigator = new Navigator();

        presenter = new PlaceDetailPresenter(place);
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

    @OnClick(R.id.btn_map)
    void onlocateClicked() {
        presenter.onLocateClicked();
    }

    @Override
    public void showPlace(Place place) {
        Glide.with(this)
                .load(place.imageUrl)
                .into(placeImageView);
        getSupportActionBar().setTitle(place.name);
        addressTv.setText(place.area);
        descriptionTv.setText("Some Random Description\nSome Random Description\nSome Random Description\nSome Random Description\nSome Random Description\nSome Random Description\nSome Random Description\nSome Random Description\nSome Random Description\nSome Random Description\nSome Random Description\nSome Random Description\nSome Random Description\nSome Random Description\nSome Random Description\nSome Random Description\nSome Random Description\nSome Random Description\nSome Random Description");
        if (place.geometry != null && place.geometry.location != null)
            mapBtn.show();
        else
            mapBtn.hide();
    }

    @Override
    public void showPlaceLocation(Place place) {
        navigator.navigateToPlaceLocationMap(this, place);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
