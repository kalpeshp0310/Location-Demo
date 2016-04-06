package com.kalpesh.locationsapp.ui.location;

import com.kalpesh.locationsapp.model.Place;
import com.kalpesh.locationsapp.ui.base.BasePresenter;
import com.kalpesh.locationsapp.ui.base.BaseView;

/**
 * Created by kalpeshpatel on 06/04/16.
 */
public class PlaceLocationPresenter implements BasePresenter<PlaceLocationPresenter.PlaceLocationView> {

    private PlaceLocationView view;
    private Place place;

    public PlaceLocationPresenter(Place place) {
        this.place = place;
    }

    @Override
    public void attachView(PlaceLocationView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    public void onMapReady() {
        view.initMap();
        view.showPlaceOnMap(place);
    }

    public interface PlaceLocationView extends BaseView {
        void initMap();

        void showPlaceOnMap(Place place);
    }
}
