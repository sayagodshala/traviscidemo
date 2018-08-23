package com.merabreak.service.challenge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.merabreak.Util;
import com.merabreak.network.Connectivity;

public class ChallengeUpdateReceiver extends BroadcastReceiver {
    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
       // Log.d("LocationUpdateReceiver", "fired");
        Intent service = new Intent(context, ChallengeService.class);
        context.stopService(service);
        if (Connectivity.isConnected(context)) {
            if (Util.getUser(context) != null) {
                context.startService(service);
            }
        }
    }
}