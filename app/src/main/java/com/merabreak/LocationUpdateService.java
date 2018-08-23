package com.merabreak;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by sayagodshala on 09/02/16.
 */
public class LocationUpdateService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    public LocationUpdateService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
