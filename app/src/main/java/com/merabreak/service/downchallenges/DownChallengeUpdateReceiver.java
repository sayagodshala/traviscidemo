package com.merabreak.service.downchallenges;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.merabreak.Util;
import com.merabreak.network.Connectivity;

/**
 * Created by Vinay on 10/17/2016.
 */
public class DownChallengeUpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       // Log.d("LocationUpdateReceiver", "fired");
        Intent service = new Intent(context, DownChallengeService.class);
        context.stopService(service);
        if (Connectivity.isConnected(context)) {
            if (Util.getUser(context) != null) {
                context.startService(service);
            }
        }
    }
}
