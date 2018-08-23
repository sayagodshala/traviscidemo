package com.merabreak;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class AppSettings {

    private static String TAG = AppSettings.class.getName();
    public static final String APP_SHARED_PREFERENCE_NAME = "com.merabreak";
    public static SharedPreferences prefs = null;
    public static final String PREF_GCM_REGISTRATION_ID = "PREF_GCM_REGISTRATION_ID";
    public static final String PREF_APP_VERSION = "PREF_APP_VERSION";
    public static final String PREF_GCM_REGISTRATION_STATUS = "PREF_GCM_REGISTRATION_STATUS";
    public static final String PREF_USER_DATA = "PREF_USER_DATA";
    public static final String PREF_USER_POINTS_WALLET = "PREF_USER_POINTS_WALLET";
    public static final String PREF_IS_USER_LOGGED_IN = "PREF_IS_USER_LOGGED_IN";
    public static final String PREF_IS_USER_FIRST_OPENED = "PREF_IS_USER_FIRST_OPENED";
    public static final String PREF_IS_USER_REFER_OPENED = "PREF_IS_USER_REFER_OPENED";
    public static final String PREF_IS_USER_SUPER_POINTS_OPENED = "PREF_IS_USER_SUPER_POINTS_OPENED";
    public static final String PREF_IS_MOBILE_VERIFIED = "PREF_IS_MOBILE_VERIFIED";
    public static final String PREF_CHALLENGES = "PREF_CHALLENGES";
    public static final String PREF_OFFLINE_DOWNLOAD = "PREF_OFFLINE_DOWNLOAD";
    public static final String PREF_ACCOUNT_DETAILS = "PREF_ACCOUNT_DETAILS";
    public static final String PREF_CATEGORIES = "PREF_CATEGORIES";
    public static final String PREF_DOWN_CHALLENGES = "PREF_DOWN_CHALLENGES";
    public static final String PREF_RECHARGE_PLANS = "PREF_RECHARGE_PLANS";
    public static final String PREF_LAT_LNG = "PREF_LAT_LNG";
    public static final String PREF_SPIN_ID = "PREF_SPIN_ID";
    public static final String PREF_HOME_CHALLENGES = "PREF_HOME_CHALLENGES";
    public static final String PREF_HOME_RAFFLES = "PREF_HOME_RAFFLES";
    public static final String PREF_HOME_STORES = "PREF_HOME_STORES";
    public static final String PREF_START_CHALLENGE = "PREF_START_CHALLENGE";
    public static final String PREF_SELECTED_CITY = "PREF_SELECTED_CITY";
    public static final String PREF_SELECTED_CITY_ID = "PREF_SELECTED_CITY_ID";
    public static final String PREF_CAMPAIGN_REFERRER = "PREF_CAMPAIGN_REFERRER";
    public static final String PREF_CAMPAIGN_CONTENT = "PREF_CAMPAIGN_CONTENT";
    public static final String PREF_CAMPAIGN_NAME = "PREF_CAMPAIGN_NAME";
    public static final String PREF_IPL_POINTS_CONFIG = "PREF_IPL_POINTS_CONFIG";
    public static final String PREF_QUIZ_RIGHT_ANSWERS = "PREF_QUIZ_RIGHT_ANSWERS";
    public static final String PREF_IS_UPDATE_PROFILE_DIALOG_VISIBLE = "PREF_IS_UPDATE_PROFILE_DIALOG_VISIBLE";
    public static final String EXTRAS_SPIN_POINTS = "EXTRAS_SPIN_POINTS";


    // IPL keys

    public static final String PREF_OWN_TEAM_SELECTED_KEY = "PREF_OWN_TEAM_SELECTED_KEY";
    public static final String PREF_IS_HOME_TEAM_SELECTED = "PREF_IS_HOME_TEAM_SELECTED";
    public static final String PREF_IPL_CONFIG = "PREF_IPL_CONFIG";

    public static String getValue(Context context, String key,
                                  String defaultValue) {
        prefs = context.getSharedPreferences(APP_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(key, defaultValue);
    }

    public static void setValue(Context context, String key, String value) {
        try {
            prefs = context.getSharedPreferences(APP_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(key, value);
            editor.apply();
        }catch (NullPointerException e)
        {
            Log.e(TAG,e.toString());
        }
        catch (Exception e)
        {
            Log.e(TAG,e.toString());
        }
    }

    public static void clearAllPrefs(Context context) {
        SharedPreferences settings = context.getSharedPreferences(
                APP_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }

    public static final String REDEEM_TYPE_RAFFLE = "redeem_type_raffle";
    public static final String REDEEM_TYPE_PRODUCT = "redeem_type_product";
}