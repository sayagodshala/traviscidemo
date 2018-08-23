package com.merabreak.pushNotifications;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.merabreak.AppSettings;
import com.merabreak.ApplicationSingleton;
import com.merabreak.Constants;
import com.merabreak.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

/**
 * Created by Saya Godshala on 1/23/2016.
 */
public class PushNotificationHelper {

    private Activity activity;
    private int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private GoogleCloudMessaging googleCloudMessaging;
    private String regId = "";

    public PushNotificationHelper(Activity activity) {
        this.activity = activity;
        checkPlayService();
    }

    private void checkPlayService() {
        // Check device for Play Services APK. If check succeeds, proceed with
        // GCM registration.
        if (checkPlayServices()) {
            googleCloudMessaging = GoogleCloudMessaging.getInstance(activity);
            regId = getRegistrationId();

            if (regId.isEmpty()) {
                registerInBackground();
            }
        } else {
            //Log.d("GCMToken", "No valid Google Play Services APK found.");
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                //Log.d("GCM", "This device is not supported.");

            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */


    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (googleCloudMessaging == null) {
                        googleCloudMessaging = GoogleCloudMessaging.getInstance(activity);
                    }
                    regId = googleCloudMessaging.register(Constants.GCM_SENDER_ID);
                    msg = "Device registered, registration ID=" + regId;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
//                    Handler h = new Handler(SplashActivity.this.getMainLooper());
//                    h.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            subscribeToPushNotifications(regId);
//                        }
//                    });

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(regId);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.d("GCMToken", msg + "\n");
            }
        }.execute(null, null, null);
    }


    private String getRegistrationId() {

        String registrationId = Util.getGCMToken(activity);
        if (registrationId.isEmpty()) {
            Log.d("GCMToken", "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = Integer.parseInt(AppSettings.getValue(activity, AppSettings.PREF_APP_VERSION, "0"));
        int currentVersion = ApplicationSingleton.getInstance().getAppVersion();
        if (registeredVersion != currentVersion) {
            Log.d("GCMToken", "App version changed.");
            return "";
        }
        return registrationId;
    }

    private void storeRegistrationId(String regId) {
        int appVersion = ApplicationSingleton.getInstance().getAppVersion();
        //Log.d("GCMToken", "Saving regId on app version " + appVersion);
        Util.setGCMToken(activity, regId);
        AppSettings.setValue(activity, AppSettings.PREF_APP_VERSION, appVersion + "");
    }


}
