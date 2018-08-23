/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.merabreak.pushNotifications;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.merabreak.Constants;
import com.merabreak.Util;
import com.merabreak.models.APIResponse;
import com.merabreak.models.User;
import com.merabreak.network.APIClient;
import com.merabreak.network.APIService;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            // In the (unlikely) event that multiple refresh operations occur simultaneously,
            // ensure that they are processed sequentially.
            synchronized (TAG) {
                // [START register_for_gcm]
                // Initially this call goes out to the network to retrieve the token, subsequent calls
                // are local.

                String gcmRegId = needToGetGCMToken();
                if (gcmRegId == null || gcmRegId.isEmpty()) {
                    // [START get_token]
                    InstanceID instanceID = InstanceID.getInstance(this);
                    String token = instanceID.getToken(Constants.GCM_SENDER_ID,
                            GoogleCloudMessaging.INSTANCE_ID_SCOPE);
                    // [END get_token]
                    //Log.i(TAG, "GCM Registration Token: " + token);

                   /* // TODO: Implement this method to send any registration to your app's servers.
                    sendRegistrationToServer(token);*/

                    // You should store a boolean that indicates whether the generated token has been
                    // sent to your server. If the boolean is false, send the token to your server,
                    // otherwise your server should have already received the token.
                    // NotificationPreferences.prefs(this).edit().putBoolean(NotificationPreferences.SENT_TOKEN_TO_SERVER, true).apply();
                    NotificationPreferences.prefs(this).edit().putString(NotificationPreferences.NOTIFICATION_REGISTRATION_TOKEN, token).apply();
                    // [END register_for_gcm]

                    // TODO: Implement this method to send any registration to your app's servers.
                    sendRegistrationToServer(token);

                } else {

                    if (!NotificationPreferences.prefs(this).getBoolean(NotificationPreferences.SENT_TOKEN_TO_SERVER,
                            false)) {

                        sendRegistrationToServer(gcmRegId);
                    }
                }
            }
        } catch (Exception e) {
            //Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            NotificationPreferences.prefs(this).edit().putBoolean(NotificationPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.

        // Intent registrationComplete = new Intent(NotificationPreferences.REGISTRATION_COMPLETE);
        // LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

    }

    /**
     * Persist registration to third-party servers.
     * <p>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        User user = Util.getUser(this);
        String deviceId = Util.uniqueDeviceID(this);
        APIService apiService = APIClient.getAPIService();
        Call<APIResponse> callback = apiService.sendTokenToServer(Constants.MB_API_KEY, user.getAuthToken(), token, deviceId);
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                if(response.code() == 200 && response.body() != null) {
                    if(response.body().getMeta() != null) {
                        if (response.body().getMeta().equals("200")) {
                            // NotificationPreferences.prefs(this).edit().putBoolean(NotificationPreferences.SENT_TOKEN_TO_SERVER, true).apply();
                            //Log.d("token to server", "token send to server");
                        } else {
                            // throw new Exception("Failed to sent token to server: " );
                        }
                    }else{

                    }
                }else{

                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private String needToGetGCMToken() {
        String gcmRegId = NotificationPreferences.prefs(this).getString(NotificationPreferences.NOTIFICATION_REGISTRATION_TOKEN, "");
        if (gcmRegId == null || gcmRegId.isEmpty()) {
            return "";
        }
        // Check if Application was updated; if so, it must clear the
        // registration ID
        // since the existing gcmRegId is not guaranteed to work with the new
        // application version.
        int registeredVersion = 1;
        int currentVersion = 1;

        if (registeredVersion != currentVersion) {
            return "";
        }
        return gcmRegId;
    }


}
