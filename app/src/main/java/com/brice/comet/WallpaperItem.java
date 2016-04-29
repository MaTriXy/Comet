package com.brice.comet;

import android.os.Parcel;
import android.os.Parcelable;

public class WallpaperItem implements Parcelable {

    String title, url;

    public WallpaperItem(String title, String url) {
        this.title = title;
        this.url = url;
    }

    protected WallpaperItem(Parcel in) {
        title = in.readString();
        url = in.readString();
    }

    public static final Creator<WallpaperItem> CREATOR = new Creator<WallpaperItem>() {
        @Override
        public WallpaperItem createFromParcel(Parcel in) {
            return new WallpaperItem(in);
        }

        @Override
        public WallpaperItem[] newArray(int size) {
            return new WallpaperItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(url);
    }
}
