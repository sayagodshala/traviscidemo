package com.merabreak.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.merabreak.AppSettings;
import com.merabreak.BaseActivity;
import com.merabreak.BuildConfig;
import com.merabreak.Constants;
import com.merabreak.MainActivity_;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.models.DeepLinkParams;
import com.merabreak.network.Connectivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {

    @ViewById(R.id.splash_main)
    RelativeLayout splashBack;

    private static final long SPLASH_WAIT_TIME = 2 * 1000L;
    private boolean canceled;
    private static final int MY_PERMISSION_FINE_LOCATION = 101;
    String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS};
    private DeepLinkParams deepLinkParams;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        canceled = true;
    }

    @AfterViews
    void init() {
        //Log.d("TOKEN SHA1", Util.getCertificateSHA1Fingerprint(this));
        //Log.d("TOKEN KEYHASH", Util.getKeyHash(this));
       // Log.d("TOKEN GCM", Util.getGCMToken(this));
       // Log.d("Device Density", Util.getDeviceDPI(this));

        Util.saveHomeChallenge(this, null);
        Util.saveHomeRaffles(this, null);
        Util.saveHomeStores(this, null);

        if (BuildConfig.FLAVOR.equalsIgnoreCase("development")) {
            try {
                URL url = new URL(Constants.BASE_URL);
                Util.makeToast(this, "Development Version : " + url.getHost());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        requestPermission();

        SharedPreferences preferences = getSharedPreferences(AppSettings.APP_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putBoolean(AppSettings.PREF_IS_UPDATE_PROFILE_DIALOG_VISIBLE, false).commit();
    }

    @Background
    void waitOnSplashFor(long milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        navigateHome();
    }

    @UiThread
    void navigateHome() {
        if (!canceled) {
            if (Util.isMobileVerified(this) || Util.isUserLoggedIn(this)) {
                if (Connectivity.isConnected(this)) {
                    MainActivity_.intent(this).deepLinkParams(deepLinkParams).start();
                } else {
                    OfflineChallengesActivity_.intent(this).start();
                }
            } else {
                MobileNumberVarificationNew_.intent(this).start();
            }
        }
        finish();
    }

    private void requestPermission() {
        if (hasPermissions(this, PERMISSIONS)) {
            waitOnSplashFor(SPLASH_WAIT_TIME);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                appAlert();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                appAlert();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                appAlert();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE)) {
                appAlert();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                appAlert();
            } else {
                ActivityCompat.requestPermissions(this,
                        PERMISSIONS,
                        MY_PERMISSION_FINE_LOCATION);
            }
        }
    }

    private void appAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.splash_perm_alert));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(SplashActivity.this,
                        PERMISSIONS,
                        MY_PERMISSION_FINE_LOCATION);
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(getResources().getColor(R.color.selected_tab_color));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (permissions.length > 0) {
            //Log.d("Permissions", permissions[0] + " : " + grantResults[0] + MY_PERMISSION_FINE_LOCATION);
            //appAlert1();
        } else {
           // Log.d("Permissions", "--------------" + MY_PERMISSION_FINE_LOCATION);
        }


        switch (requestCode) {
            case MY_PERMISSION_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    waitOnSplashFor(SPLASH_WAIT_TIME);
                } else {
                    appAlert();
                }
                return;
            }
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Branch branch = Branch.getInstance();
        branch.initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {

                    deepLinkParams = new Gson().fromJson(referringParams.toString(), DeepLinkParams.class);
                    //Log.d("Deep Link", new Gson().toJson(deepLinkParams));
                    if (deepLinkParams.isClickedBranchLink()) {
                        //Log.d("Deep Link", true + "");
                    } else {
                       // Log.d("Deep Link", false + "");
                    }
//                    if (deepLinkParams.getNameValuePairs() != null) {
//                        Log.d("Deep Link", "Has Name Value Pairs");
//                    } else {
//                        Log.d("Deep Link", "No Name Value Pairs");
//                    }

//                    try {
//                        Log.d("Deep Link", referringParams.toString());
//                        if (referringParams.has("nameValuePairs")) {
//                            JSONObject nameValuePairs = referringParams.getJSONObject("nameValuePairs");
//                            if (nameValuePairs.has("+clicked_branch_link") && nameValuePairs.getBoolean("+clicked_branch_link")) {
//                                Log.d("Deep Link", true + "");
//                            } else {
//                                Log.d("Deep Link", false + "");
//                            }
//                        } else {
//                            Log.d("Deep Link", "No Name Value Pairs");
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }

                    // params are the deep linked params associated with the link that the user clicked -> was re-directed to this app
                    // params will be empty if no data found
                    // ... insert custom logic here ...
                } else {
                    Log.d("MyApp", error.getMessage());
                }
            }
        }, this.getIntent().getData(), this);
    }
}
