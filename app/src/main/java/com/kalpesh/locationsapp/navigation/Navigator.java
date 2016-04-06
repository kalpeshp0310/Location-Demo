package com.kalpesh.locationsapp.navigation;

import android.content.Context;

import com.kalpesh.locationsapp.model.Place;
import com.kalpesh.locationsapp.ui.location.PlaceLocationActivity;
import com.kalpesh.locationsapp.ui.nearByPlaces.NearByPlacesActivity;
import com.kalpesh.locationsapp.ui.placeDetail.PlaceDetailActivity;

/**
 * Created by kalpeshpatel on 06/04/16.
 */
public class Navigator {

    public void navigateToPlacesList(Context context, double lat, double lng) {
        if (context != null) {
            context.startActivity(NearByPlacesActivity.getCallingIntent(context, lat, lng));
        }
    }

    public void navigateToPlaceDetailScreen(Context context, Place place) {
        if (context != null)
            context.startActivity(PlaceDetailActivity.getCallingIntent(context, place));
    }

    public void navigateToPlaceLocationMap(Context context, Place place) {
        if (context != null)
            context.startActivity(PlaceLocationActivity.getCallingIntent(context, place));
    }
}
