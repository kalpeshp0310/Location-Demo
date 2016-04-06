package com.kalpesh.locationsapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kalpeshpatel on 06/04/16.
 */
public class PlacePhoto implements Parcelable {
    public int height;
    public int width;
    @SerializedName("photo_reference")
    public String photoRefernce;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.height);
        dest.writeInt(this.width);
        dest.writeString(this.photoRefernce);
    }

    public PlacePhoto() {
    }

    protected PlacePhoto(Parcel in) {
        this.height = in.readInt();
        this.width = in.readInt();
        this.photoRefernce = in.readString();
    }

    public static final Parcelable.Creator<PlacePhoto> CREATOR = new Parcelable.Creator<PlacePhoto>() {
        @Override
        public PlacePhoto createFromParcel(Parcel source) {
            return new PlacePhoto(source);
        }

        @Override
        public PlacePhoto[] newArray(int size) {
            return new PlacePhoto[size];
        }
    };
}
