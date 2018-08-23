package com.merabreak.utils;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

import java.io.InputStream;

public class GifView extends View {
    public Movie mMovie;
    public long movieStart;
    private int gifId;

    public GifView(Context context) {
        super(context);
    }

    public GifView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView(attrs.getAttributeResourceValue("http://schemas.android.com/apk/res-auto", "src", 0));
    }

    public GifView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeView(attrs.getAttributeResourceValue("http://schemas.android.com/apk/res-auto", "src", 0));
    }

    private void initializeView(final int id) {
        InputStream is = getContext().getResources().openRawResource(id);
        mMovie = Movie.decodeStream(is);
        this.gifId = id;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        super.onDraw(canvas);
        long now = android.os.SystemClock.uptimeMillis();

        if (movieStart == 0) {
            movieStart = now;
        }

        if (mMovie != null) {
            int relTime = (int) ((now - movieStart) % mMovie.duration());
            mMovie.setTime(relTime);
            mMovie.draw(canvas, getWidth() - mMovie.width(), getHeight() - mMovie.height());
            this.invalidate();
        }
    }

    public void setGIFResource(int resId) {
        this.gifId = resId;
        initializeView(this.gifId);
    }

    public int getGIFResource() {
        return this.gifId;
    }
}