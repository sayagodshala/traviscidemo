package com.merabreak.models;

import android.graphics.drawable.Drawable;

/**
 * Created by Vinay on 12/6/2016.
 */
public class GridImagesModel {
    Drawable image;
    String id;

    public GridImagesModel(Drawable d, String id){
        image = d;
        this.id = id;
    }


    public Drawable getImage() {
        return image;
    }


    public String getId() {
        return id;
    }

}
