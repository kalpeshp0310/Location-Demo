package com.kalpesh.locationsapp.ui.nearByPlaces;

import com.kalpesh.locationsapp.data.LocationsApi;
import com.kalpesh.locationsapp.data.model.BaseException;
import com.kalpesh.locationsapp.Constants;
import com.kalpesh.locationsapp.model.Place;
import com.kalpesh.locationsapp.ui.base.BasePresenter;
import com.kalpesh.locationsapp.ui.base.BaseView;
import com.kalpesh.locationsapp.utils.ConnectivityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kalpeshpatel on 06/04/16.
 */
public class NearByPlacesPresenter implements BasePresenter<NearByPlacesPresenter.NearByPlacesView> {

    private static final int DEFAULT_RADIUS_TO_SEARCH = 2000;
    private static final String[] DEFAULT_PLACES_TO_SEARCH = {"food", "gym", "school", "hospital", "spa", "restaurant"};

    private double latitude;
    private double longitude;
    private LocationsApi locationsApi;
    private NearByPlacesView view;
    private CompositeSubscription compositeSubscription;

    public NearByPlacesPresenter(double lat, double lng) {
        this.latitude = lat;
        this.longitude = lng;
        compositeSubscription = new CompositeSubscription();
    }

    public void attachView(NearByPlacesView nearByPlacesView) {
        view = nearByPlacesView;
        locationsApi = new LocationsApi(nearByPlacesView.getContext());
        if (ConnectivityUtils.isConnectedToInternet(view.getContext())) {
            getPlaces();
        } else {
            view.showErrorMessage("Please turn on Internet connection");
        }
    }

    private void getPlaces() {
        view.setShowLoading(true);
        Subscription subscription = locationsApi.getNearByPlaces(latitude, longitude, DEFAULT_RADIUS_TO_SEARCH, Arrays.asList(DEFAULT_PLACES_TO_SEARCH))
                .map(nearbySearchResponse -> {
                    for (int i = 0; i < nearbySearchResponse.places.size(); i++) {
                        Place place = nearbySearchResponse.places.get(i);
                        if (place.placePhotos != null && !place.placePhotos.isEmpty()) {
                            place.imageUrl = String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=%s&key=%s", place.placePhotos.get(0).photoRefernce, Constants.LOCATION_API_KEY);
                        }
                        nearbySearchResponse.places.set(i, place);
                    }
                    return nearbySearchResponse.places;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        places -> {
                            view.setShowLoading(false);
                            view.showNearByPlaces(places);
                        },
                        throwable -> {
                            view.setShowLoading(false);
                            String errorMessage;
                            if (throwable instanceof IOException)
                                errorMessage = "Something went wrong while connecting to our servers. Please try again after some time.";
                            else if (throwable instanceof BaseException)
                                errorMessage = throwable.getMessage();
                            else
                                errorMessage = "Sorry, there seems to be an Error!";
                            view.showErrorMessage(errorMessage);
                        });
        compositeSubscription.add(subscription);
    }

    public void detachView() {
        view = null;
        compositeSubscription.clear();
    }

    public void onPlaceSelected(Place place) {
        view.showPlaceDetail(place);
    }

    interface NearByPlacesView extends BaseView {
        void showNearByPlaces(List<Place> places);

        void showPlaceDetail(Place place);
    }
}
