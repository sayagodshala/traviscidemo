package com.merabreak.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ewebcoremac1 on 06/07/18.
 */

public class PrizeModel implements Parcelable{


    private String title;
    private String thumb_image;

    protected PrizeModel(Parcel in) {
        title = in.readString();
        thumb_image = in.readString();
    }

    public static final Creator<PrizeModel> CREATOR = new Creator<PrizeModel>() {
        @Override
        public PrizeModel createFromParcel(Parcel in) {
            return new PrizeModel(in);
        }

        @Override
        public PrizeModel[] newArray(int size) {
            return new PrizeModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(thumb_image);
    }
}
