package com.merabreak;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by sayagodshala on 09/02/16.
 */
public class AlarmLocationUpdate extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, LocationUpdateService.class);
        context.startService(i);
    }
}
