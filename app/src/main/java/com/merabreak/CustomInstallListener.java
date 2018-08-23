package com.merabreak;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.branch.referral.InstallListener;

/**
 * Created by Vinay on 12/21/2016.
 */
public class CustomInstallListener extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        InstallListener installListener = new InstallListener();
        installListener.onReceive(context, intent);
    }
}
