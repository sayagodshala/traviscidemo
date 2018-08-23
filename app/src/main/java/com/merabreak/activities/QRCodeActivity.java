package com.merabreak.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;
import com.merabreak.BaseActivity;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ETPL-002 on 03-May-17.
 */
@EActivity(R.layout.activity_qr_main)
public class QRCodeActivity extends BaseActivity {

    @ViewById(R.id.scanbtn)
    Button scanbtn;

    @ViewById(R.id.result)
    TextView result;

    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;

    @AfterViews
    void init(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }
    }

    @Click(R.id.scanbtn)
    void scanQR(){
        Intent intent = new Intent(this, ScanActivity_.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if(data != null){
                final Barcode barcode = data.getParcelableExtra("barcode");
                result.post(new Runnable() {
                    @Override
                    public void run() {
                       // result.setText(barcode.displayValue);
                      //  Util.makeToast(QRCodeActivity.this,barcode.displayValue);
                        ChallengeDetailActivity_.intent(QRCodeActivity.this).slug(barcode.displayValue).start();
                       // finish();
                    }
                });
            }
        }
    }
}
