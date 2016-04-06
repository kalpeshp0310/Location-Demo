package com.kalpesh.locationsapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kalpeshpatel on 06/04/16.
 */
public class Location implements Parcelable {
    public double lat;
    public double lng;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
    }

    public Location() {
    }

    protected Location(Parcel in) {
        this.lat = in.readDouble();
        this.lng = in.readDouble();
    }

    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
