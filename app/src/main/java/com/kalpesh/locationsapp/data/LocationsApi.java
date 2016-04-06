package com.kalpesh.locationsapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kalpesh.locationsapp.data.model.BaseException;
import com.kalpesh.locationsapp.data.model.NearbySearchResponse;
import com.kalpesh.locationsapp.Constants;
import com.kalpesh.locationsapp.model.Location;
import com.kalpesh.locationsapp.utils.ListUtils;

import org.json.JSONObject;

import java.util.List;

import okhttp3.Response;
import rx.Completable;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kalpeshpatel on 06/04/16.
 */
public class LocationsApi {

    private final static String LOCATION_PREFS = "location_preferences";
    private final static String LAST_KNOWN_LATITUDE = "lat_lat";
    private final static String LAST_KNOWN_LONGITUDE = "last_lng";

    private Context context;
    private Gson gson;

    public LocationsApi(Context context) {
        this.context = context;
        gson = new GsonBuilder().serializeNulls().create();
    }

    public Observable<Location> getLastKnownLocation() {
        return Observable.fromCallable(() -> {
            SharedPreferences preferences = context.getSharedPreferences(LOCATION_PREFS, Context.MODE_PRIVATE);
            double lat = preferences.getFloat(LAST_KNOWN_LATITUDE, 0);
            double lng = preferences.getFloat(LAST_KNOWN_LONGITUDE, 0);
            Location location = new Location();
            location.lat = lat;
            location.lng = lng;
            return location;
        });
    }

    public void saveLastKnownLocation(double lat, double lng) {
        Completable.fromAction(() -> {
            SharedPreferences preferences = context.getSharedPreferences(LOCATION_PREFS, Context.MODE_PRIVATE);
            preferences.edit().putFloat(LAST_KNOWN_LATITUDE, (float) lat)
                    .putFloat(LAST_KNOWN_LONGITUDE, (float) lng)
                    .commit();
        }).subscribe();
    }

    public Observable<NearbySearchResponse> getNearByPlaces(double lat, double lng, long radius, List<String> types) {
        String url = String.format("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%f,%f&radius=%d&type=%s&key=%s", lat, lng, radius, ListUtils.convetListToString(types, "|"), Constants.LOCATION_API_KEY);
        return NetworkingApi.get(url)
                .flatMap(new NearbySearchResponseParser());
    }

    private class NearbySearchResponseParser implements Func1<Response, Observable<NearbySearchResponse>> {
        @Override
        public Observable<NearbySearchResponse> call(Response response) {
            try {
                String responseString = response.body().string();
                JSONObject jsonObject = new JSONObject(responseString);
                if (response.code() == 200 && "OK".equals(jsonObject.getString("status")))
                    return Observable.just(gson.fromJson(responseString, NearbySearchResponse.class));
                else
                    return Observable.error(new BaseException(jsonObject.getString("error_message")));
            } catch (Exception e) {
                return Observable.error(e);
            }
        }
    }


}
