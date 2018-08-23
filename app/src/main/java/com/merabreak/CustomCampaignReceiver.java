package com.merabreak;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.analytics.CampaignTrackingReceiver;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ETPL-002 on 11-Oct-17.
 */

public class CustomCampaignReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        String referrer = intent.getStringExtra("referrer");
        Tracker gaTracker = ((ApplicationSingleton) context.getApplicationContext()).googleAnalyticsTracker();
        if(referrer != null) {
            gaTracker.send(new HitBuilders.EventBuilder().setCampaignParamsFromUrl(referrer).build());
            Util.saveCampaignReferrer(context, referrer);
            try {
                HashMap<String, String> getParams = Utility.getHashMapFromQuery(referrer);
                String campaignName = getParams.get("utm_campaign");
                if(campaignName != null ) {
                    if(campaignName.equalsIgnoreCase("challenge")) {
                        String source = getParams.get("utm_content");
                        if (source != null)
                            Util.saveCampaignContent(context, source);
                    }else if (campaignName.equalsIgnoreCase("promocode")){
                        String source = getParams.get("utm_content");
                        if (source != null)
                            Util.saveCampaignName(context, source);
                    }
                }

            }catch (UnsupportedEncodingException e){
                Log.e("Referrer Error", e.getMessage());
            }
        }
        new CampaignTrackingReceiver().onReceive(context, intent);
    }
}
