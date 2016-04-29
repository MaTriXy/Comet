package com.brice.comet;

import android.os.Parcel;
import android.os.Parcelable;

public class IconItem implements Parcelable {

    String name;
    int icon;

    public IconItem(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    protected IconItem(Parcel in) {
        name = in.readString();
        icon = in.readInt();
    }

    public static final Creator<IconItem> CREATOR = new Creator<IconItem>() {
        @Override
        public IconItem createFromParcel(Parcel in) {
            return new IconItem(in);
        }

        @Override
        public IconItem[] newArray(int size) {
            return new IconItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(icon);
    }
}
