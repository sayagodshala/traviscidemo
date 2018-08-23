package com.merabreak.activities;

import android.animation.ObjectAnimator;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.merabreak.BaseActivity;
import com.merabreak.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

/**
 * Created by ETPL-002 on 03-May-17.
 */
@EActivity(R.layout.activity_scan)
public class ScanActivity extends BaseActivity {

    BarcodeDetector barcode;
    CameraSource cameraSource;
    SurfaceHolder holder, holder1;

    @ViewById(R.id.cameraView)
    SurfaceView cameraView;

    @ViewById(R.id.cameraView_dummy)
    SurfaceView cameraView_dummy;

    @ViewById(R.id.scanner)
    ImageView scanner;

    @ViewById(R.id.close_scan)
    Button close_scan;

    @ViewById(R.id.text1)
    TextView text1;

    @ViewById(R.id.scanner_indicator)
    View scanner_indicator;

    Animation animation;

    @AfterViews
    void init(){

        cameraView.setZOrderMediaOverlay(true);
        cameraView_dummy.setZOrderMediaOverlay(true);
        holder = cameraView.getHolder();
        holder1 = cameraView_dummy.getHolder();
        barcode = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        if(!barcode.isOperational()){
            Toast.makeText(getApplicationContext(), "Sorry, Couldn't setup the detector", Toast.LENGTH_LONG).show();
            this.finish();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        Animation trophyAnim = AnimationUtils.loadAnimation(ScanActivity.this, R.anim.slide_down);
        scanner_indicator.startAnimation(trophyAnim);

        cameraSource = new CameraSource.Builder(this, barcode)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(24)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1024,768)
                .build();
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try{
                    if(ContextCompat.checkSelfPermission(ScanActivity.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        cameraSource.start(cameraView.getHolder());
                    }
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
        barcode.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes =  detections.getDetectedItems();
                if(barcodes.size() > 0){
                   /* Intent intent = new Intent();
                    intent.putExtra("barcode", barcodes.valueAt(0));
                    setResult(RESULT_OK, intent);
                    finish();*/
                    ChallengeDetailActivity_.intent(ScanActivity.this).slug(barcodes.valueAt(0).displayValue).start();
                    finish();
                }
            }
        });

        cameraView_dummy.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try{
                    if(ContextCompat.checkSelfPermission(ScanActivity.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        cameraSource.start(cameraView_dummy.getHolder());
                    }
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
    }

    private void setFontType(){
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/roboto_medium.ttf");
        text1.setTypeface(font);
    }

    @Override
    public void onResume() {
        super.onResume();
        setFontType();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.stop();
    }

    @Click(R.id.close_scan)
    void closeScan(){
        finish();
    }
}
