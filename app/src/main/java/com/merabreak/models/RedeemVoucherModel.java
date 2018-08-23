package com.merabreak.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ewebcoremac1 on 07/07/18.
 */

public class RedeemVoucherModel implements Parcelable{

    private String title;
    private String slug;
    private String icon_image;
    private String bg_image;
    private int raffles = 0;
    private int products = 0;

    protected RedeemVoucherModel(Parcel in) {
        title = in.readString();
        slug = in.readString();
        icon_image = in.readString();
        bg_image = in.readString();
        raffles = in.readInt();
        products = in.readInt();
    }

    public static final Creator<RedeemVoucherModel> CREATOR = new Creator<RedeemVoucherModel>() {
        @Override
        public RedeemVoucherModel createFromParcel(Parcel in) {
            return new RedeemVoucherModel(in);
        }

        @Override
        public RedeemVoucherModel[] newArray(int size) {
            return new RedeemVoucherModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getIcon_image() {
        return icon_image;
    }

    public void setIcon_image(String icon_image) {
        this.icon_image = icon_image;
    }

    public String getBg_image() {
        return bg_image;
    }

    public void setBg_image(String bg_image) {
        this.bg_image = bg_image;
    }

    public int getRaffles() {
        return raffles;
    }

    public void setRaffles(int raffles) {
        this.raffles = raffles;
    }

    public int getProducts() {
        return products;
    }

    public void setProducts(int products) {
        this.products = products;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(slug);
        dest.writeString(icon_image);
        dest.writeString(bg_image);
        dest.writeInt(raffles);
        dest.writeInt(products);
    }
}
