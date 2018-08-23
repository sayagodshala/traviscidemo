package com.merabreak.pushNotifications;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Vinay on 11/12/2016.
 */
public class NotificationPreferences {

    private static final String NOTIFICATION_PREFERENCE = "com.drivify.driver.notification-prefs";
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String NOTIFICATION_REGISTRATION_TOKEN = "notification_registration_token";

    public static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(NOTIFICATION_PREFERENCE, 0);
    }

    public static void clearPrefs(Context context) {
        prefs(context).edit().putBoolean(SENT_TOKEN_TO_SERVER, false);
        prefs(context).edit().putString(NOTIFICATION_REGISTRATION_TOKEN, "");
    }

    public static String token(Context context) {
        return prefs(context).getString(NotificationPreferences.NOTIFICATION_REGISTRATION_TOKEN, "");
    }
}
