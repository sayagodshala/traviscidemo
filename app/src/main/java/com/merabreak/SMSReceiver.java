package com.merabreak;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

public class SMSReceiver extends BroadcastReceiver {

    final SmsManager sms = SmsManager.getDefault();

    private boolean once = true;

    public void onReceive(Context context, Intent intent) {
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber.trim();
                    String message = currentMessage.getDisplayMessageBody();
                    //Log.d("SmsReceiver", senderNum + " , " + message);
                    //    if (senderNum.toLowerCase().contains("620")) {
                    for (int j = 0; j < Constants.SMS_SENDER_IDs.length; j++) {
                        if (senderNum.toLowerCase().contains(Constants.SMS_SENDER_IDs[j])) {
                            String otp = otp(message);
                            //Log.d("SMSOTP", otp);
                            if (once) {
                                once = false;

                                if (otp.length() > 3) {
                                    //Log.d("SMSOTP1", otp);
                                    BusProvider.bus().post(new OTPRecievedEvent(otp(message)));
                                }
                            }
                            return;
                        }
                    }
                }
            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }
    }

    private String otp(String message) {
        String getAllNumbers = message.replaceAll("[^-?0-9]+", " ");
        List<String> numbersAsList = Arrays.asList(getAllNumbers.trim().split(" "));
        if (numbersAsList.size() > 0)
            return numbersAsList.get(numbersAsList.size() - 1);
        return "";
    }
}