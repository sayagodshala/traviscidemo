package com.merabreak.imageloader;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;


public class ImageLoaderTask extends AsyncTask<Void, Void, Void> {
    ImageView mImageView;
    String mImageURL;
    Bitmap mBitmap;
    Context mContext;
    Dialog mProgressDialog;
    boolean mShowProgressDialog = true;
    int mSize = 0;

    // private ImageLoaderTask(Context context, ImageView imageView, String url,
    // boolean showProgress) {
    // this(context, imageView, url, showProgress, 0);
    // }

    public ImageLoaderTask(Context context, ImageView imageView, String url,
                           boolean showProgress, int size) {
        mContext = context;
        mImageView = imageView;
        mImageURL = url;
        mShowProgressDialog = showProgress;
        mSize = size;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

//		if (mShowProgressDialog)
//			mProgressDialog = ProgressHUD.show(mContext, "", true, true, null,
//					true);
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            URL url = new URL(mImageURL);

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(url.openConnection().getInputStream(),
                    null, o);

            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            if (mSize > 0)
                while (true) {
                    if (width_tmp / 2 < mSize)
                        break;
                    width_tmp /= 2;
                    height_tmp /= 2;
                    scale *= 2;
                }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            mBitmap = BitmapFactory.decodeStream(url.openConnection()
                    .getInputStream(), null, o2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if (mProgressDialog != null && mProgressDialog.isShowing()
                && mProgressDialog.getWindow() != null)
            try {
                mProgressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }

        if (mBitmap != null)
            mImageView.setImageBitmap(mBitmap);
    }

}