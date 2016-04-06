package com.kalpesh.locationsapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kalpeshpatel on 06/04/16.
 */
public class Place implements Parcelable {
    @Expose
    public Geometry geometry;

    @Expose
    public String icon;

    @Expose
    public String id;

    @Expose
    public String name;

    @Expose
    @SerializedName("photos")
    public List<PlacePhoto> placePhotos;

    @Expose
    public String placeId;

    public String imageUrl;

    @Expose
    public float rating;

    @Expose
    public List<String> types;

    @Expose
    @SerializedName("vicinity")
    public String area;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.geometry, flags);
        dest.writeString(this.icon);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeList(this.placePhotos);
        dest.writeString(this.placeId);
        dest.writeString(this.imageUrl);
        dest.writeFloat(this.rating);
        dest.writeStringList(this.types);
        dest.writeString(this.area);
    }

    public Place() {
    }

    protected Place(Parcel in) {
        this.geometry = in.readParcelable(Geometry.class.getClassLoader());
        this.icon = in.readString();
        this.id = in.readString();
        this.name = in.readString();
        this.placePhotos = new ArrayList<>();
        in.readList(this.placePhotos, PlacePhoto.class.getClassLoader());
        this.placeId = in.readString();
        this.imageUrl = in.readString();
        this.rating = in.readFloat();
        this.types = in.createStringArrayList();
        this.area = in.readString();
    }

    public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel source) {
            return new Place(source);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };
}
