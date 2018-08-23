package com.merabreak;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatDrawableManager;
import android.widget.Button;
import android.widget.GridLayout;

import com.merabreak.models.GridImagesModel;

/**
 * Created by Vinay on 10/26/2016.
 */
public class MemoryButton extends Button {

    protected int r;
    protected int c;
    protected GridImagesModel showingImageId;

    protected Drawable drawables;

    protected boolean isFlipped = false;
    public boolean isMatched = false;

    protected Drawable front;
    protected Drawable back;
    int id;
    public MemoryButton(Context context, int row, int column, GridImagesModel showingImageId, int id ){//int showingImage){

        super(context);
        r = row;
        c = column;
        this.showingImageId = showingImageId;
        this.id = id;
        front = showingImageId.getImage();// AppCompatDrawableManager.get().getDrawable(context,showingImage);
        back = AppCompatDrawableManager.get().getDrawable(context, R.drawable.blank);
        setBackground(back);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(row), GridLayout.spec(column));
        params.setMargins(3,3,3,3);
        params.width = (int) getResources().getDisplayMetrics().density * 50;
        params.height = (int) getResources().getDisplayMetrics().density * 50;
       /* params.width = 150;
        params.height = 150;*/
        setLayoutParams(params);
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setIsMatched(boolean isMatched) {
        this.isMatched = isMatched;
    }

    /*public int getShowingImageId() {
        return showingImageId;
    }*/

    public String getShowingImageId() {
        return showingImageId.getId();
    }

    public void flip(){
        if(isMatched) {
            return;
        }
        if(isFlipped){
            setBackground(back);
            isFlipped = false;
        }else{
            setBackground(front);
            isFlipped = true;
        }
    }
}
