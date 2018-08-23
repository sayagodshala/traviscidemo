package com.merabreak;

import android.content.Context;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Vinay on 11/22/2016.
 */
public class GoogleAnalyticEvents {

    public static void eventAppLoaded(Context context, Tracker tracker, String action, String label) {
        tracker.send(new HitBuilders.EventBuilder().
                setCategory("App Loaded").
                setAction(action).
                setLabel(label).
                build());
    }

    public static void eventLogin(Context context, Tracker tracker, String category, String action, String label) {
        tracker.send(new HitBuilders.EventBuilder().
                setCategory(category).
                setAction(action).
                setLabel(label).
                build());
    }

    public static void eventSignup(Context context, Tracker tracker, String category, String action, String label) {
        tracker.send(new HitBuilders.EventBuilder().
                setCategory(category).
                setAction(action).
                setLabel(label).
                build());
    }

    public static void eventChallengeLike(Context context, Tracker tracker, String action, String label) {
        tracker.send(new HitBuilders.EventBuilder().
                setCategory("Challenge Like").
                setAction(action).
                setLabel(label).
                build());
    }

    public static void event(Context context, Tracker tracker, String category, String action, String label) {
        tracker.send(new HitBuilders.EventBuilder().
                setCategory(category).
                setAction(action).
                setLabel(label).
                build());
    }

    public static void eventLogout(Context context, Tracker tracker, String category, String action, String label, String value) {
        tracker.send(new HitBuilders.EventBuilder().
                setCategory(category).
                setAction(action).
                setLabel(label).
                set("User Details: ", value).
                build());
    }

    public static void eventCampaign(Context context, Tracker tracker, String category, String action, String label, String user) {
        tracker.send(new HitBuilders.EventBuilder().
                setCategory(category).
                setAction(action).
                setCampaignParamsFromUrl(label).
                set("UserRegdNum", user).
                build());
    }
}
