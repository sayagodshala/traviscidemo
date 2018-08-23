package com.merabreak;

import android.app.NotificationManager;
import android.content.Context;
import android.view.View;

import com.merabreak.controls.FailureAlert;
import com.merabreak.controls.FailureAlert_;
import com.merabreak.controls.SuccessAlert;
import com.merabreak.controls.SuccessAlert_;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class Utility {
    public static void dummyWait() {
        try {
            Thread.sleep(Constants.ASYNC_LOAD_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean randomSuccess() {
        return (new Random().nextInt(10) + 1) % 2 == 0;
    }

    public static View successMessageView(Context context, String messageRes) {
        SuccessAlert successAlert = SuccessAlert_.build(context, null);
        successAlert.setMessage(messageRes);
        return successAlert;
    }

    public static View successMessageView(Context context, int messageRes) {
        SuccessAlert successAlert = SuccessAlert_.build(context, null);
        successAlert.setMessage(messageRes);
        return successAlert;
    }

    public static View failureMessageView(Context context, String messageRes) {
        FailureAlert failureAlert = FailureAlert_.build(context, null);
        failureAlert.setMessage(messageRes);
        return failureAlert;
    }

    public static View failureMessageView(Context context, int messageRes) {
        FailureAlert failureAlert = FailureAlert_.build(context, null);
        failureAlert.setMessage(messageRes);
        return failureAlert;
    }

    public static void failureMessage(Context context, String message) {
        View messageView = Utility.failureMessageView(context, message);
//        new MaterialDialog.Builder(context)
//                .customView(messageView, false)
//                .positiveText("Ok")
//                .show();//Akanksha
    }

    public static HashMap<String, String> getHashMapFromQuery(String query)
            throws UnsupportedEncodingException {

        HashMap<String, String> query_pairs = new LinkedHashMap<String, String>();

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
                    URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }

    public static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
    }
}
