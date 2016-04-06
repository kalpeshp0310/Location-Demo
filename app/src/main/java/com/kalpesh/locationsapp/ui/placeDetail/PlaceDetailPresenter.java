package com.kalpesh.locationsapp.ui.placeDetail;

import com.kalpesh.locationsapp.model.Place;
import com.kalpesh.locationsapp.ui.base.BasePresenter;
import com.kalpesh.locationsapp.ui.base.BaseView;

/**
 * Created by kalpeshpatel on 06/04/16.
 */
public class PlaceDetailPresenter implements BasePresenter<PlaceDetailPresenter.PlaceDetailView> {

    private PlaceDetailView view;
    private Place place;

    public PlaceDetailPresenter(Place place) {
        this.place = place;
    }

    @Override
    public void attachView(PlaceDetailView view) {
        this.view = view;
        if (place != null)
            view.showPlace(place);
    }

    @Override
    public void detachView() {
        view = null;
    }

    public void onLocateClicked() {
        view.showPlaceLocation(place);
    }

    interface PlaceDetailView extends BaseView {
        void showPlace(Place place);

        void showPlaceLocation(Place place);
    }
}
