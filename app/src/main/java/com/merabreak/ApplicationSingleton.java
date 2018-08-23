package com.merabreak;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.applinks.AppLinkData;
import com.facebook.stetho.Stetho;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.gson.Gson;
import com.liulishuo.filedownloader.FileDownloader;
import com.merabreak.activities.ChallengeDetailActivity_;
import com.merabreak.activities.GaneshaSpeaksActivity_;
import com.merabreak.activities.ProfileActivity_;
import com.merabreak.activities.RaffleActivity_;
import com.merabreak.activities.RedeemPointsActivity_;
import com.merabreak.activities.StoreActivity_;
import com.merabreak.activities.WinnersGalleryActivity_;
import com.merabreak.models.IPLConfig;
import com.merabreak.network.ConnectivityReceiver;
import com.merabreak.service.challenge.ChallengeUpdateReceiver;
import com.merabreak.service.downchallenges.DownChallengeUpdateReceiver;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.net.URISyntaxException;

import io.branch.referral.Branch;

import io.fabric.sdk.android.Fabric;

public class ApplicationSingleton extends Application implements LocationClient.LocationClientListener, ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String TAG = ApplicationSingleton.class.getSimpleName();

    private LocationClient locationClient;

    private static ApplicationSingleton instance;

    public static ApplicationSingleton getInstance() {
        return instance;
    }

    private AlarmManager challengeUpdateAlarmManager;
    private PendingIntent challengeUpdateReceiverIntent;

    public static GoogleAnalytics analytics;
    public static Tracker tracker;

    public static boolean isChallengePlaying = false;

    /*DeepstreamClient deepstreamClient = null;
    DeepstreamClient challengeContestdeepstreamClient = null;*/
    public ConnectivityReceiver connectivityReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        if (ConnectivityReceiver.isConnected(this)) {
           // initDeepstreamFactory();
            //initChallengeContestDeepstreamFactory();
        }

        setConnectivityListener(this);

        Stetho.initializeWithDefaults(this);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .autoPromptLocation(true)
                .setNotificationReceivedHandler(new NotoifcationReceiveHandler())
                .setNotificationOpenedHandler(new NotificationHandler())
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        Branch.getAutoInstance(this);
        Branch.enableLogging();
        Fabric.with(this, new Crashlytics());
        FileDownloader.init(this);
        isChallengePlaying = false;
        //Log.d(TAG, "onCreate");
        instance = this;
        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);
        AppLinkData.fetchDeferredAppLinkData(this,
                new AppLinkData.CompletionHandler() {
                    @Override
                    public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
                        if (appLinkData != null) {
                            Bundle bundle = appLinkData.getArgumentBundle();
                            //Log.d("Facebook deeplink data", bundle.toString());
                        }
                    }
                }
        );
        updateChallenge();
        /*locationClient = new LocationClient(getApplicationContext(), this);
        locationClient.connect();*/
        locationClient();
        downChallenge();

       // appsFlyerLib().setGCMProjectID(Constants.GCM_SENDER_ID);
       // appsFlyerLib().startTracking(this, Constants.AF_DEV_KEY);
        Branch.getAutoInstance(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public int getAppVersion() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    synchronized public Tracker googleAnalyticsTracker() {
        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(5);
        analytics.getLogger().setLogLevel(com.google.android.gms.analytics.Logger.LogLevel.VERBOSE);
        tracker = analytics.newTracker("UA-87825414-1");
        tracker.enableExceptionReporting(true);
//        tracker.enableAdvertisingIdCollection(true);
//        tracker.enableAutoActivityTracking(true);
        return tracker;
    }

    public void locationClient() {
        locationClient = new LocationClient(getApplicationContext(), this);
        locationClient.connect();
    }

   /* public AppsFlyerLib appsFlyerLib() {
        return AppsFlyerLib.getInstance();
    }*/

    private void updateChallenge() {
        Intent alarmIntent = new Intent(this, ChallengeUpdateReceiver.class);
        challengeUpdateReceiverIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        challengeUpdateAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long interval = 1000 * 30;
        challengeUpdateAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, challengeUpdateReceiverIntent);
    }

    private void downChallenge() {
        Intent alarmIntent = new Intent(this, DownChallengeUpdateReceiver.class);
        challengeUpdateReceiverIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        challengeUpdateAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long interval = 1000 * 30;
        challengeUpdateAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, challengeUpdateReceiverIntent);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (locationClient != null) {
            locationClient.disconnect();
            locationClient = null;
            if (location != null) {
                Util.saveLatLng(getApplicationContext(), location.getLatitude() + "," + location.getLongitude());
                //Log.d("Location Changed Single", location.getLatitude() + "," + location.getLongitude());
                BusProvider.bus().post(new LocationEvent(location));
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (locationClient != null) {
            locationClient.disconnect();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        /*if (isConnected) {
            initDeepstreamFactory();
            //initChallengeContestDeepstreamFactory();
        } else {
            closeDeepStream();
            //closeChallengeContestDeepStream();
        }*/
    }

    private class NotificationHandler implements OneSignal.NotificationOpenedHandler {

        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;
            //Log.d("OnesignalNoti1", new Gson().toJson(data));
            String customKey = null;

            if (data != null) {
                customKey = data.optString("flag", null);
                //if (customKey != null)
                    //Log.i("OneSignalExample", "customkey set with value: " + customKey);
            }
            Intent intent;
            if (customKey != null) {
                if (customKey.equals("1")) {
                    intent = new Intent(getApplicationContext(), ChallengeDetailActivity_.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("slug", data.optString("slug"));
                    startActivity(intent);
                } else if (customKey.equals("4")) {
                    intent = new Intent(getApplicationContext(), StoreActivity_.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("id", data.optString("slug"));
                    startActivity(intent);
                } else if (customKey.equals("5")) {
                    intent = new Intent(getApplicationContext(), RaffleActivity_.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("id", data.optString("slug"));
                    startActivity(intent);
                } else if (customKey.equals("6")) {
                    intent = new Intent(getApplicationContext(), ProfileActivity_.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (customKey.equals("7")) {
                    intent = new Intent(getApplicationContext(), ProfileActivity_.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (customKey.equals("10")) {
                    intent = new Intent(getApplicationContext(), RedeemPointsActivity_.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (customKey.equals("14")) {
                    intent = new Intent(getApplicationContext(), GaneshaSpeaksActivity_.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (customKey.equals("15")) {
                    intent = new Intent(getApplicationContext(), WinnersGalleryActivity_.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else {
                    intent = new Intent(getApplicationContext(), MainActivity_.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            } else {
                intent = new Intent(getApplicationContext(), MainActivity_.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

       /* if (actionType == OSNotificationAction.ActionType.ActionTaken)
            Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);*/
        }
    }


    private class NotoifcationReceiveHandler implements OneSignal.NotificationReceivedHandler {

        @Override
        public void notificationReceived(OSNotification notification) {
            JSONObject data = notification.payload.additionalData;
            Log.d("OnesignalNoti1rec", new Gson().toJson(notification));
            String customKey;

            if (data != null) {
                customKey = data.optString("flag", null);
                //if (customKey != null)
                   /// Log.i("OneSignalExample", "customkey set with value: " + customKey);
            }

            if (isChallengePlaying) {
                OneSignal.clearOneSignalNotifications();
            }
        }
    }

    /*private void initDeepstreamFactory() {
        IPLConfig iplConfig = Util.getIPLConfig(this);
        DeepstreamFactory factory = DeepstreamFactory.getInstance();
        try {
            deepstreamClient = factory.getClient(iplConfig.getSocketUrl());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        deepstreamClient.login();
        deepstreamClient.setRuntimeErrorHandler(new DeepstreamRuntimeErrorHandler() {
            @Override
            public void onException(Topic topic, Event event, String s) {
                //Log.d(event.name(), topic.name() + " : " + s);
            }
        });
    }

    public void closeDeepStream() {
        if (deepstreamClient != null) {
            deepstreamClient.close();
            deepstreamClient.removeConnectionChangeListener(null);
            deepstreamClient = null;
        }
    }

    public DeepstreamClient getDeepstreamClient() {
        if(deepstreamClient == null) {
            initDeepstreamFactory();
        }
        return deepstreamClient;
    }*/

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}