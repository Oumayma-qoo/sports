package com.example.events_live.domain.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Ads implements Parcelable {
    String image_path,redirect_url,open_type;

    public Ads(String image_path, String redirect_url, String open_type) {
        this.image_path = image_path;
        this.redirect_url = redirect_url;
        this.open_type = open_type;
    }

    protected Ads(Parcel in) {
        image_path = in.readString();
        redirect_url = in.readString();
        open_type = in.readString();
    }

    public static final Creator<Ads> CREATOR = new Creator<Ads>() {
        @Override
        public Ads createFromParcel(Parcel in) {
            return new Ads(in);
        }

        @Override
        public Ads[] newArray(int size) {
            return new Ads[size];
        }
    };

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getRedirect_url() {
        return redirect_url;
    }

    public void setRedirect_url(String redirect_url) {
        this.redirect_url = redirect_url;
    }

    public String getOpen_type() {
        return open_type;
    }

    public void setOpen_type(String open_type) {
        this.open_type = open_type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image_path);
        dest.writeString(redirect_url);
        dest.writeString(open_type);
    }
}
