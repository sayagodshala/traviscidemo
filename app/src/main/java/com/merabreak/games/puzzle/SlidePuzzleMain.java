package com.merabreak.games.puzzle;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dolby.dap.DolbyAudioProcessing;
import com.merabreak.BaseActivity;
import com.merabreak.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.MessageFormat;


public class SlidePuzzleMain extends BaseActivity implements SlidePuzzleView.SlidePuzzleListener {

    protected static final String KEY_SHOW_NUMBERS = "showNumbers";
    protected static final String KEY_IMAGE_URI = "imageUri";
    protected static final String KEY_PUZZLE = "slidePuzzle";
    protected static final String KEY_PUZZLE_SIZE = "puzzleSize";

    protected static final String FILENAME_DIR = "com.dolby.DolbyPuzzle";
    protected static final String FILENAME_PHOTO_DIR = FILENAME_DIR + "/photo";

    protected static final int DEFAULT_SIZE = 3;

    private SlidePuzzleView view;
    private SlidePuzzle slidePuzzle;
    private Options bitmapOptions;
    private int puzzleWidth = 1;
    private int puzzleHeight = 1;
    private Uri imageUri;
    private boolean portrait;
    private boolean expert;

    DolbyAudioProcessing mDolbyAudioProcessing;
    private final java.util.List<String> mActList = new java.util.ArrayList<String>();
    private Bitmap puzzleBitmap;
    private RelativeLayout container;
    private int targetWidth;
    private int targetHeight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jigsaw_puzzle);

        container = (RelativeLayout) this.findViewById(R.id.container_jigsaw);

        bitmapOptions = new Options();
        bitmapOptions.inScaled = false;

        slidePuzzle = new SlidePuzzle();

        view = new SlidePuzzleView(this, slidePuzzle, this);

        container.addView(view);

        shuffle();

        setPuzzleSize(DEFAULT_SIZE, true);

        targetWidth = view.getTargetWidth();
        targetHeight = view.getTargetHeight();

        new DownloadImageFromURL().execute("http://cadmin.merabreak.com/assets/images/challenges/jigsaw/1463724396mqQGT.png");

    }

    private void shuffle() {
        slidePuzzle.init(puzzleWidth, puzzleHeight);
        slidePuzzle.shuffle();
        view.invalidate();
        expert = view.getShowNumbers() == SlidePuzzleView.ShowNumbers.NONE;
    }

    public Options getBitmapOptions() {
        Options o = new Options();
        o.inJustDecodeBounds = true;

        if (o.outWidth > o.outHeight && targetWidth < targetHeight) {
            int i = targetWidth;
            targetWidth = targetHeight;
            targetHeight = i;
        }

        if (targetWidth < o.outWidth || targetHeight < o.outHeight) {
            double widthRatio = (double) targetWidth / (double) o.outWidth;
            double heightRatio = (double) targetHeight / (double) o.outHeight;
            double ratio = Math.max(widthRatio, heightRatio);

            o.inSampleSize = (int) Math.pow(2, (int) Math.round(Math.log(ratio) / Math.log(0.5)));
        } else {
            o.inSampleSize = 1;
        }

        o.inScaled = false;
        o.inJustDecodeBounds = false;

        return o;
    }

    protected void loadBitmap(Uri uri) {
        try {
            InputStream imageStream = getContentResolver().openInputStream(uri);

            imageStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream, null, getBitmapOptions());

            if (bitmap == null) {
                Toast.makeText(this, getString(R.string.error_could_not_load_image), Toast.LENGTH_LONG).show();
                return;
            }
            setBitmap(bitmap);
            imageUri = uri;
        } catch (FileNotFoundException ex) {
            Toast.makeText(this, MessageFormat.format(getString(R.string.error_could_not_load_image_error), ex.getMessage()), Toast.LENGTH_LONG).show();
            return;
        }
    }


    private void setBitmap(Bitmap bitmap) {
        portrait = bitmap.getWidth() < bitmap.getHeight();

        view.setBitmap(bitmap);
        setPuzzleSize(Math.min(puzzleWidth, puzzleHeight), true);

//        setRequestedOrientation(portrait ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

//    private void selectImage() {
//        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//        photoPickerIntent.setType("image/*");
//        startActivityForResult(photoPickerIntent, RESULT_SELECT_IMAGE);
//    }

//    private void takePicture() {
//        File dir = getSaveDirectory();
//
//        if (dir == null) {
//            Toast.makeText(this, getString(R.string.error_could_not_create_directory_to_store_photo), Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        File file = new File(dir, FILENAME_PHOTO);
//        Intent photoPickerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//        startActivityForResult(photoPickerIntent, RESULT_TAKE_PHOTO);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
//        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
//
//        switch (requestCode) {
//            case RESULT_SELECT_IMAGE: {
//                if (resultCode == RESULT_OK) {
//                    Uri selectedImage = imageReturnedIntent.getData();
//                    loadBitmap(selectedImage);
//                }
//
//                break;
//            }
//
//            case RESULT_TAKE_PHOTO: {
//                if (resultCode == RESULT_OK) {
//                    File file = new File(getSaveDirectory(), FILENAME_PHOTO);
//
//                    if (file.exists()) {
//                        Uri uri = Uri.fromFile(file);
//
//                        if (uri != null) {
//                            loadBitmap(uri);
//                        }
//                    }
//                }
//
//                break;
//            }
//        }
//    }

    private File getSaveDirectory() {
        File root = new File(Environment.getExternalStorageDirectory().getPath());
        File dir = new File(root, FILENAME_PHOTO_DIR);

        if (!dir.exists()) {
            if (!root.exists() || !dir.mkdirs()) {
                return null;
            }
        }

        return dir;
    }

    private float getImageAspectRatio() {
        Bitmap bitmap = view.getBitmap();

        if (bitmap == null) {
            return 1;
        }

        float width = bitmap.getWidth();
        float height = bitmap.getHeight();

        return width / height;
    }

    protected void setPuzzleSize(int size, boolean scramble) {
        float ratio = getImageAspectRatio();

        if (ratio < 1) {
            ratio = 1f / ratio;
        }

        int newWidth;
        int newHeight;

        if (portrait) {
            newWidth = size;
            newHeight = (int) (size * ratio);
        } else {
            newWidth = (int) (size * ratio);
            newHeight = size;
        }

        if (scramble || newWidth != puzzleWidth || newHeight != puzzleHeight) {
            puzzleWidth = newWidth;
            puzzleHeight = newHeight;
            shuffle();
        }
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//
//        onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        boolean hasCamera = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
//
//        menu.add(0, MENU_SELECT_IMAGE, 0, R.string.menu_select_image);
//
//        if (hasCamera) {
//            menu.add(0, MENU_TAKE_PHOTO, 0, R.string.menu_take_photo);
//        }
//
//        menu.add(0, MENU_SCRAMBLE, 0, R.string.menu_scramble);
//
//        return true;
//    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        return onOptionsItemSelected(item);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case MENU_SCRAMBLE:
//                shuffle();
//                return true;
//
//            case MENU_SELECT_IMAGE:
//                selectImage();
//                return true;
//
//            case MENU_TAKE_PHOTO:
//                takePicture();
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    protected SharedPreferences getPreferences() {
        return getSharedPreferences(SlidePuzzleMain.class.getName(), Activity.MODE_PRIVATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

//        if (mPlayer != null) {
//            mPlayer.pause();
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();

//        if (mPlayer != null) {
//            mPlayer.start();
//        }
    }

//    protected boolean loadPreferences() {
//        SharedPreferences prefs = getPreferences();
//
//        try {
//
//            String s = prefs.getString(KEY_IMAGE_URI, null);
//
//            if (s == null) {
//                imageUri = null;
//            } else {
//                loadBitmap(Uri.parse(s));
//            }
//
//            int size = prefs.getInt(KEY_PUZZLE_SIZE, 0);
//            s = prefs.getString(KEY_PUZZLE, null);
//
//            if (size > 0 && s != null) {
//                String[] tileStrings = s.split("\\;");
//
//                if (tileStrings.length / size > 1) {
//                    setPuzzleSize(size, false);
//                    slidePuzzle.init(puzzleWidth, puzzleHeight);
//
//                    int[] tiles = new int[tileStrings.length];
//
//                    for (int i = 0; i < tiles.length; i++) {
//                        try {
//                            tiles[i] = Integer.parseInt(tileStrings[i]);
//                        } catch (NumberFormatException ex) {
//                        }
//                    }
//
//                    slidePuzzle.setTiles(tiles);
//                }
//            }
//
//            return prefs.contains(KEY_SHOW_NUMBERS);
//        } catch (ClassCastException ex) {
//            // ignore broken settings
//            return false;
//        }
//    }

//    public void playSound() {
//        if (mPlayer == null) {
//            mPlayer = MediaPlayer.create(
//                    SlidePuzzleMain.this,
//                    R.raw.slide);
//            mPlayer.start();
//        } else {
//            mPlayer.release();
//            mPlayer = null;
//            mPlayer = MediaPlayer.create(
//                    SlidePuzzleMain.this,
//                    R.raw.slide);
//            mPlayer.start();
//        }
//
//        mDolbyAudioProcessing = DolbyAudioProcessing.getDolbyAudioProcessing(this, DolbyAudioProcessing.PROFILE.GAME, this);
//        if (mDolbyAudioProcessing == null) {
//            return;
//        }
//    }

    public void onFinish() {
//        if (mPlayer == null) {
//            mPlayer = MediaPlayer.create(
//                    SlidePuzzleMain.this,
//                    R.raw.fireworks);
//            mPlayer.start();
//        } else {
//            mPlayer.release();
//            mPlayer = null;
//            mPlayer = MediaPlayer.create(
//                    SlidePuzzleMain.this,
//                    R.raw.fireworks);
//            mPlayer.start();
//        }
//
//        mDolbyAudioProcessing = DolbyAudioProcessing.getDolbyAudioProcessing(this, DolbyAudioProcessing.PROFILE.GAME, this);
//        if (mDolbyAudioProcessing == null) {
//            Toast.makeText(this,
//                    "Dolby Audio Processing not available on this device.",
//                    Toast.LENGTH_SHORT).show();
//            shuffle();
//        }

        shuffle();

    }

//    @Override
//    public void onCompletion(MediaPlayer mp) {
//        if (mPlayer != null) {
//            mPlayer.release();
//            mPlayer = null;
//        }
//    }
//
//    @Override
//    public void onDolbyAudioProcessingClientConnected() {
//        mDolbyAudioProcessing.setEnabled(true);
//    }
//
//    @Override
//    public void onDolbyAudioProcessingClientDisconnected() {
//        mDolbyAudioProcessing.setEnabled(false);
//    }
//
//    @Override
//    public void onDolbyAudioProcessingEnabled(boolean b) {
//    }
//
//    @Override
//    public void onDolbyAudioProcessingProfileSelected(DolbyAudioProcessing.PROFILE profile) {
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Log.d("Dolby processing", "onDestroy()");

        // Release Media Player instance
//        if (mPlayer != null) {
//            mPlayer.release();
//            mPlayer = null;
//        }
//
//        this.releaseDolbyAudioProcessing();

    }

    @Override
    public void onResume() {
        super.onResume();
//        restartSession();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Log.d("Dolby processing", "The application is in background, supsendSession");
        //
        // If audio playback is not required while your application is in the background, restore the Dolby audio processing system
        // configuration to its original state by suspendSession().
        // This ensures that the use of the system-wide audio processing is sandboxed to your application.
//        suspendSession();
    }

    public void releaseDolbyAudioProcessing() {
        if (mDolbyAudioProcessing != null) {
            try {
                mDolbyAudioProcessing.release();
                mDolbyAudioProcessing = null;
            } catch (IllegalStateException ex) {
                handleIllegalStateException(ex);
            } catch (RuntimeException ex) {
                handleRuntimeException(ex);
            }
        }

    }

    // Backup the system-wide audio effect configuration and restore the application configuration
//    public void restartSession() {
//        if (mDolbyAudioProcessing != null) {
//            try {
//                mDolbyAudioProcessing.restartSession();
//            } catch (IllegalStateException ex) {
//                handleIllegalStateException(ex);
//            } catch (RuntimeException ex) {
//                handleRuntimeException(ex);
//            }
//        }
//    }

    // Backup the application Dolby Audio Processing configuration and restore the system-wide configuration
//    public void suspendSession() {
//
//        if (mDolbyAudioProcessing != null) {
//            try {
//                mDolbyAudioProcessing.suspendSession();
//            } catch (IllegalStateException ex) {
//                handleIllegalStateException(ex);
//            } catch (RuntimeException ex) {
//                handleRuntimeException(ex);
//            }
//        }
//    }

    /**
     * Generic handler for IllegalStateException
     */
    private void handleIllegalStateException(Exception ex) {
        //Log.e("Dolby processing", "Dolby Audio Processing has a wrong state");
        handleGenericException(ex);
    }

    /**
     * Generic handler for IllegalArgumentException
     */
    private void handleIllegalArgumentException(Exception ex) {
        //Log.e("Dolby processing", "One of the passed arguments is invalid");
        handleGenericException(ex);
    }

    /**
     * Generic handler for RuntimeException
     */
    private void handleRuntimeException(Exception ex) {
        //Log.e("Dolby processing", "Internal error occured in Dolby Audio Processing");
        handleGenericException(ex);
    }

    private void handleGenericException(Exception ex) {
        //Log.e("Dolby processing", Log.getStackTraceString(ex));
    }

    @Override
    public void isSolved() {

    }

    class DownloadImageFromURL extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                java.net.URL url = new java.net.URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                puzzleBitmap = BitmapFactory.decodeStream(input);

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            // Dismiss the dialog after the Music file was downloaded
            setBitmap(puzzleBitmap);
        }
    }


}
