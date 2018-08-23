package com.merabreak.service.challenge;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.merabreak.Util;
import com.merabreak.models.User;
import com.merabreak.network.Connectivity;

import java.util.Calendar;

public class ChallengeSyncReceiver extends BroadcastReceiver {

    // restart service every 30 seconds
    private static final long REPEAT_TIME = 1000 * 30;

    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.d("ChallengeSyncReceiver", "fired");
        if (Connectivity.isConnected(context)) {
            User user = Util.getUser(context);
            if (user != null) {
                AlarmManager service = (AlarmManager) context
                        .getSystemService(Context.ALARM_SERVICE);
                Intent i = new Intent(context, ChallengeUpdateReceiver.class);
                PendingIntent pending = PendingIntent.getBroadcast(context, 0, i, 0);
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, 10);
                // fetch every 30 seconds
                // InexactRepeating allows Android to optimize the energy consumption
                service.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        cal.getTimeInMillis(), REPEAT_TIME, pending);
            }
        }
    }
} 