package com.merabreak.pushNotifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.merabreak.ApplicationSingleton;
import com.merabreak.BusProvider;
import com.merabreak.MainActivity_;
import com.merabreak.R;
import com.merabreak.activities.ChallengePlayActivity_;
import com.merabreak.activities.ChallengeDetailActivity_;
import com.merabreak.activities.ProfileActivity_;
import com.merabreak.activities.RaffleActivity_;
import com.merabreak.activities.RedeemPointsActivity_;
import com.merabreak.activities.StoreActivity_;
import com.merabreak.pushNotifications.events.PushType;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Vinay on 11/12/2016.
 */
public class GcmNotificationListenerService extends GcmListenerService {

    android.graphics.Bitmap largeImage;

    String notiMsg;

    /**
     * Called when message is received.
     *
     * @param from   SenderID of the sender.
     * @param extras Data bundle containing message data as key/value pairs.
     *               For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle extras) {
        //Log.i(this.getClass().getSimpleName(), "Received message");

       /* String data = extras.getString("details");

         String msg=extras.getString("gcm_msg").toString();*/

        Context context = this;
        if (!isAuthorized(context)) return;

        /*Bundle[{gcm_msg={"flag":"2","msg":"Driver reached to ower location","details":[{"ride_id":"61","driver_id":"25","user_id":"119","gcm_token":"cTOBzklUWxE:APA91bGfJfBN-jajOf72upUjkxfzb8c6PbYkkzIP5tByx-GaeaZI6USxcSWJAArRH9uWtrHJTao5rZ7wDCHqqt1iW4HBlWGHCK839f3XP_YXTNibaN81O7LmXMiryiEvgyxOZ1MHqR48","device_type":"Android"}]}, collapse_key=do_not_collapse}]
         *
         * mesag#####{"msg":"Booking Information","flag":"0","details":[{"user_end_location":"19.2183307,72.9780897","ride_id":"3","driver_id":"25","user_id":"119","phone":"8898757165","device_type":"Android","user_location":"19.065409,72.9990928","gcm_token":"fnh24yl1BVk:APA91bHvM9QBtkrq1y7-5EbnwHDeNXK0r-tIEp5KuIxJHdzFL5DjlGseJBe2VZe_grcOdcpwsRpnsvv6TBkgcgrBHKVycS0R_oDoSkDNIEWuKJADRTNDgWSoe2GHVbLGJtyWJi3Th3gx","end_location_name":"Thane, Maharashtra, India","username":"Abbas Sayyed","start_location_name":"Inorbit Bypass, Sector 30A, Vashi"}]}*/


        String message = extras.getString("fcm_msg");
        //System.out.println("mesag#####" + message);

        if (message != null) {

            try {
                //  NotificationType type = Enum.valueOf(NotificationType.class, extras.getString("type").toUpperCase());

                PushType pushType = new Gson().fromJson(message, new TypeToken<PushType>() {
                }.getType());

                notiMsg = pushType.getMessage();
                String title = pushType.getDetails().get(0).getTitle();
                if (pushType.getDetails().get(0).getCoverImage() != null)
                    largeImage = BitmapFactory.decodeStream((InputStream) new URL(pushType.getDetails().get(0).getCoverImage()).getContent());

                //   PushType pushType = new PushType();
                //   pushType.setType(message);
                BusProvider.bus().post(pushType);

                NotificationType type = NotificationType.fromInt(Integer.parseInt(pushType.getType()));
                switch (type) {
                    case NEW_CHALLENGE:
                        showPushNotification(message, notiMsg, title, false);
                        break;
                    case NEW_OFFER:
                        showPushNotification(message, notiMsg, title, false);
                        break;
                    case FOR_GUEST:
                        showPushNotification(message, notiMsg, title, false);
                        break;
                    case NEW_PRODUCT:
                        showPushNotification(message, notiMsg, title, false);
                        break;
                    case NEW_RAFFLE:
                        showPushNotification(message, notiMsg, title, false);
                        break;
                    case REFFERER_REGD:
                        showPushNotification(message, notiMsg, title, false);
                        break;
                    case USER_LEVELUP:
                        showPushNotification(message, notiMsg, title, false);
                        break;
                    case RAFFLE_WINNER:
                        showPushNotification(message, notiMsg, title, false);
                        break;
                    case LIVE_CHALLENGE:
                        showPushNotification(message, notiMsg, title, false);
                        break;
                    case DLT_UNKNOWN:
                        // generateNotification(context,mess);C
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isAuthorized(Context context) {
        return true;
    }

    private void showPushNotification(String data, String msg, String msg1, boolean cancelFlag) {
        Drawable myDrawable = getResources().getDrawable(R.drawable.logo_new);
        Bitmap anImage = ((BitmapDrawable) myDrawable).getBitmap();
        android.support.v4.app.NotificationCompat.Builder mBuilder =
                new android.support.v4.app.NotificationCompat.Builder(this)
                        .setLargeIcon(anImage)
                        .setContentTitle(msg1)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg1))
                        .setContentText(msg);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setSmallIcon(R.drawable.logo_new);
            mBuilder.setColor(getResources().getColor(R.color.selected_tab_color));
        } else {
            mBuilder.setSmallIcon(R.drawable.logo_new);
        }
        if (largeImage != null) {
            android.support.v4.app.NotificationCompat.BigPictureStyle bigPic = new NotificationCompat.BigPictureStyle();
            bigPic.bigPicture(largeImage);
            bigPic.setSummaryText(notiMsg);
            mBuilder.setStyle(bigPic);

        }
        //resultIntent.putExtra("filterType", "New");
        // Creates an explicit intent for an Activity in your app
        PushType pushType = new Gson().fromJson(data, new TypeToken<PushType>() {
        }.getType());
        Intent resultIntent;
        if (pushType.getType().equals("1")) {
            resultIntent = new Intent(this, ChallengeDetailActivity_.class);
            resultIntent.putExtra("slug", pushType.getDetails().get(0).getSlug());
            resultIntent.putExtra("NOTI_DATA", data);
        } else if (pushType.getType().equals("4")) {
            resultIntent = new Intent(this, StoreActivity_.class);
            resultIntent.putExtra("id", pushType.getDetails().get(0).getSlug());
            resultIntent.putExtra("NOTI_DATA", data);
        } else if (pushType.getType().equals("5")) {
            resultIntent = new Intent(this, RaffleActivity_.class);
            resultIntent.putExtra("id", pushType.getDetails().get(0).getSlug());
            resultIntent.putExtra("NOTI_DATA", data);
        } else if (pushType.getType().equals("6")) {
            resultIntent = new Intent(this, ProfileActivity_.class);
            resultIntent.putExtra("NOTI_DATA", data);
        } else if (pushType.getType().equals("7")) {
            resultIntent = new Intent(this, ProfileActivity_.class);
            resultIntent.putExtra("NOTI_DATA", data);
        } else if (pushType.getType().equals("9")) {
            resultIntent = new Intent(this, ChallengePlayActivity_.class);
            resultIntent.putExtra("slug", pushType.getDetails().get(0).getSlug());
            resultIntent.putExtra("NOTI_DATA", data);
        } else if (pushType.getType().equals("10")) {
            resultIntent = new Intent(this, RedeemPointsActivity_.class);
            resultIntent.putExtra("NOTI_DATA", data);
        } else {
            resultIntent = new Intent(this, MainActivity_.class);
            resultIntent.putExtra("NOTI_DATA", data);
            System.out.println("data####" + data);
        }

        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(notifyPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (cancelFlag) {
            mBuilder.setOngoing(true);
            mBuilder.build().flags |= Notification.FLAG_NO_CLEAR;
        } else {
            mBuilder.setAutoCancel(true);
            mBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;
        }

        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        mBuilder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
        if (((ApplicationSingleton) getApplication()).isChallengePlaying) {
            mBuilder.setPriority(NotificationCompat.PRIORITY_LOW);
        } else {
            mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        mBuilder.setLights(Color.RED, 3000, 3000);
        mNotificationManager.notify(1001, mBuilder.build());

        if (((ApplicationSingleton) getApplication()).isChallengePlaying) {
            mNotificationManager.cancel(1001);
        }
    }
}
