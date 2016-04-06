package com.kalpesh.locationsapp.data.model;

import com.google.gson.annotations.SerializedName;
import com.kalpesh.locationsapp.model.Place;

import java.util.List;

/**
 * Created by kalpeshpatel on 06/04/16.
 */
public class NearbySearchResponse {
    @SerializedName("next_page_token")
    public String nextPageToken;
    @SerializedName("results")
    public List<Place> places;
}
