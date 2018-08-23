package com.merabreak.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {

    private int REQUIRED_SIZE = 70;
    private boolean mIsCC = false;

    MemoryCache memoryCache = new MemoryCache();
    FileCache fileCache;
    private Map<ImageView, String> imageViews = Collections
            .synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService;
    Handler handler = new Handler();// handler to display images in UI thread

    public ImageLoader(Context context) {
        fileCache = new FileCache(context);
        executorService = Executors.newFixedThreadPool(5);
    }

    public ImageLoader(Context context, boolean noLooper) {
        fileCache = new FileCache(context);
    }

    // ///////// // final int stub_id = R.drawable.black_fade;

    public Bitmap loadBitmap(String url) {
        if (url == null || url.equalsIgnoreCase(""))
            return null;

        Bitmap bitmap = memoryCache.get(url);

        if (bitmap == null)
            bitmap = getBitmap(url);
        return bitmap;
    }

    public void DisplayImage(String url, ImageView imageView) {
        DisplayImage(url, imageView, REQUIRED_SIZE, null, 0);
    }

    public void DisplayImage(String url, ImageView imageView,
                             ProgressBar progress) {
        DisplayImage(url, imageView, REQUIRED_SIZE, progress, 0);
    }

    public void DisplayImage(String url, ImageView imageView,
                             ProgressBar progress, int defaultDrwabale) {
        DisplayImage(url, imageView, REQUIRED_SIZE, progress, defaultDrwabale);
    }

    public void DisplayImage(String url, ImageView imageView, int size) {
        DisplayImage(url, imageView, size, null, 0);
    }

    public void DisplayImage(String url, ImageView imageView, int size,
                             boolean isCC) {
        mIsCC = isCC;
        DisplayImage(url, imageView, size, null, 0);
    }

    public void DisplayImage(String url, ImageView imageView, int size,
                             ProgressBar progress, int defaultDrwabale, boolean isCC) {
        mIsCC = isCC;
        DisplayImage(url, imageView, size, progress, defaultDrwabale);
    }

    public void DisplayImage(String url, ImageView imageView, int size,
                             ProgressBar progress, int defaultDrwabale) {
        if (url == null || url.equalsIgnoreCase(""))
            return;

        if (progress != null)
            progress.setVisibility(View.VISIBLE);

        REQUIRED_SIZE = size;

        imageViews.put(imageView, url);
        Bitmap bitmap = memoryCache.get(url);

        if (bitmap != null) {
            BitmapDrawable bd = new BitmapDrawable(bitmap);
            bd.setAntiAlias(true);
            bd.setFilterBitmap(true);
            imageView.setImageDrawable(bd);
            if (progress != null)
                progress.setVisibility(View.INVISIBLE);
        } else {
            queuePhoto(url, imageView, progress, defaultDrwabale);
            // ///////// // imageView.setImageResource(stub_id);
        }
    }

    private void queuePhoto(String url, ImageView imageView,
                            ProgressBar progress, int defaultDrwabale) {
        PhotoToLoad p = new PhotoToLoad(url, imageView, progress,
                defaultDrwabale);
        executorService.submit(new PhotosLoader(p));
    }

    private Bitmap getBitmap(String url) {
        File f = fileCache.getFile(url);

        // from SD cache
        Bitmap b = decodeFile(f);
        if (b != null)
            return b;

        // from web
        try {
            Bitmap bitmap = null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl
                    .openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            CopyStream(is, os);
            os.close();
            conn.disconnect();
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Throwable ex) {
            ex.printStackTrace();
            if (ex instanceof OutOfMemoryError)
                memoryCache.clear();
            return null;
        }
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    // decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f) {
        if (!mIsCC)
            return decodeFileNormal(f);
        else
            return decodeFileCC(f);
    }

    private Bitmap decodeFileNormal(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1 = new FileInputStream(f);
            BitmapFactory.decodeStream(stream1, null, o);
            stream1.close();

            // Find the correct scale value. It should be the power of 2.
            // final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            if (REQUIRED_SIZE > 0)
                while (true) {
                    if (width_tmp / 2 < REQUIRED_SIZE
                            || height_tmp / 2 < REQUIRED_SIZE)
                        break;
                    width_tmp /= 2;
                    height_tmp /= 2;
                    scale *= 2;
                }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap decodeFileCC(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1 = new FileInputStream(f);
            BitmapFactory.decodeStream(stream1, null, o);
            stream1.close();

            // Find the correct scale value. It should be the power of 2.
            // final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            if (REQUIRED_SIZE > 0)
                while (true) {
                    if (width_tmp / 2 < REQUIRED_SIZE)
                        break;
                    width_tmp /= 2;
                    height_tmp /= 2;
                    scale *= 2;
                }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Task for the queue
    private class PhotoToLoad {
        public String url;
        public ImageView imageView;
        public ProgressBar progress;
        public int defaultDrawable;

        // public PhotoToLoad(String u, ImageView i) {
        // url = u;
        // imageView = i;
        // }

        public PhotoToLoad(String u, ImageView i, ProgressBar pb, int resId) {
            url = u;
            imageView = i;
            progress = pb;
            defaultDrawable = resId;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            try {
                if (imageViewReused(photoToLoad))
                    return;
                Bitmap bmp = getBitmap(photoToLoad.url);
                memoryCache.put(photoToLoad.url, bmp);
                if (imageViewReused(photoToLoad))
                    return;
                BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
                handler.post(bd);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.imageView);
        if (tag == null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    // Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        @Override
        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            if (bitmap != null) {
                photoToLoad.imageView.setImageBitmap(bitmap);
                if (photoToLoad.progress != null)
                    photoToLoad.progress.setVisibility(View.INVISIBLE);
            } else {
                if (photoToLoad.progress != null)
                    photoToLoad.progress.setVisibility(View.INVISIBLE);
                if (photoToLoad.defaultDrawable != 0)
                    photoToLoad.imageView
                            .setImageResource(photoToLoad.defaultDrawable);
            }
            // ///////// // else
            // ///////// // photoToLoad.imageView.setImageResource(stub_id);
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

}
