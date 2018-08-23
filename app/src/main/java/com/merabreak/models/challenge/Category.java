package com.merabreak.models.challenge;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sayagodshala on 09/03/16.
 */
public class Category implements Parcelable {

    private String id;
    private String title;
    private String slug;
    private String description;
    private String color;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Category() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.slug);
        dest.writeString(this.description);
        dest.writeString(this.color);
    }

    protected Category(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.slug = in.readString();
        this.description = in.readString();
        this.color = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
