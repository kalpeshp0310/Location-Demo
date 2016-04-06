package com.kalpesh.locationsapp.ui.placePicker;

import android.Manifest;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.kalpesh.locationsapp.data.LocationsApi;
import com.kalpesh.locationsapp.model.Location;
import com.kalpesh.locationsapp.ui.base.BasePresenter;
import com.kalpesh.locationsapp.ui.base.BaseView;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.concurrent.TimeUnit;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.OnErrorNotImplementedException;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kalpeshpatel on 06/04/16.
 */
public class MapPresenter implements BasePresenter<MapPresenter.MapView> {
    private MapView view;
    private CompositeSubscription compositeSubscription;
    private Subscription locationUpdateSubscription;
    private ReactiveLocationProvider locationProvider;
    private LocationsApi locationsApi;

    public MapPresenter() {
        compositeSubscription = new CompositeSubscription();
    }

    public void attachView(MapView mapView) {
        view = mapView;
        locationsApi = new LocationsApi(view.getContext());
    }

    void onLocationSelected(LatLng latLng) {
        view.showNearbyPlaces(latLng.latitude, latLng.longitude);
    }

    void onMapReady() {
        // Checking for Location Permission
        RxPermissions.getInstance(view.getContext())
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        checkForLocationSettings();
                    } else {
                        view.showErrorMessage("Please grant access to Location Service");
                    }
                });
    }

    private void checkForLocationSettings() {
        locationProvider = new ReactiveLocationProvider(view.getContext().getApplicationContext());

        Subscription settingsSubscription = locationProvider
                .checkLocationSettings(
                        new LocationSettingsRequest.Builder()
                                .addLocationRequest(getLocationRequest())
                                .setAlwaysShow(true)
                                .build()
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(locationSettingsResult -> {
                    Status status = locationSettingsResult.getStatus();
                    if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        view.requestEnableLocationSetting(status);
                    } else {
                        view.initMap();
                        updateLocationOnMap();
                    }
                });
        compositeSubscription.add(settingsSubscription);
    }

    private LocationRequest getLocationRequest() {
        return LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1)
                .setInterval(100);
    }

    public void onLocationSettingEnabled() {
        view.initMap();
        updateLocationOnMap();
    }

    public void onLocationSettingCanceled() {
        view.showErrorMessage("You need to enable Location Settings in order to use this App.");
    }

    public void onLocationSettingsError() {
        view.showErrorMessage("Some unrecoverable Error occurred contact developer");
    }

    private void updateLocationOnMap() {
        locationUpdateSubscription = locationProvider.getUpdatedLocation(getLocationRequest())
                .map(location -> {
                    Location location1 = new Location();
                    location1.lat = location.getLatitude();
                    location1.lng = location.getLongitude();
                    return location1;
                })
                .doOnNext(location2 -> locationsApi.saveLastKnownLocation(location2.lat, location2.lng))
                .timeout(10, TimeUnit.SECONDS)
                .onErrorResumeNext(locationsApi.getLastKnownLocation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        location -> {
                            view.moveToLocation(location);
                            locationUpdateSubscription.unsubscribe();
                        },
                        throwable -> {
                            //We will never get Exception in this callback.
                            //But if somehow we do get the this will help us to give us exact stacktrace.
                            throw new OnErrorNotImplementedException(throwable);
                        });
        compositeSubscription.add(locationUpdateSubscription);
    }

    public void detachView() {
        view = null;
        compositeSubscription.clear();
    }

    public interface MapView extends BaseView {

        void moveToLocation(Location location);

        void initMap();

        void requestEnableLocationSetting(Status status);

        void showNearbyPlaces(double lat, double lng);
    }
}
