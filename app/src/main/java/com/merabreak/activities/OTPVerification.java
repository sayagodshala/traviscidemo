package com.merabreak.activities;

import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.merabreak.ApplicationSingleton;
import com.merabreak.BaseActivity;
import com.merabreak.BusProvider;
import com.merabreak.Constants;
import com.merabreak.GoogleAnalyticEvents;
import com.merabreak.MainActivity;
import com.merabreak.OTPRecievedEvent;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.models.APIResponse;
import com.merabreak.models.DownloadedChallenges;
import com.merabreak.models.User;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by Vinay on 8/23/2016.
 */
@EActivity(R.layout.otp_verification)
public class OTPVerification extends BaseActivity {

    @ViewById(R.id.otp_mobile)
    TextView enteredMobile;

    @ViewById(R.id.change_otp_mobile)
    TextView changeMobile;

    @ViewById(R.id.progress)
    LinearLayout progress;

    @ViewById(R.id.otp_one)
    EditText otpOne;

    @ViewById(R.id.otp_two)
    EditText otpTwo;

    @ViewById(R.id.otp_three)
    EditText otpThree;

    @ViewById(R.id.otp_four)
    EditText otpFour;

    @ViewById(R.id.resend_code)
    LinearLayout resendOTP;

    @ViewById(R.id.verify_otp)
    Button verifiyOTP;

    @Extra
    String mobile;

    @AfterViews
    void init() {

        BusProvider.bus().register(this);
        enteredMobile.setText(mobile);
        otpFour.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    verifiyOTP();
                    return true;
                }
                return false;
            }
        });
    }

    private boolean isOtpValid() {
        String validationMessage = "";
        if (otpOne.getText().toString().equalsIgnoreCase("")
                || otpTwo.getText().toString().equalsIgnoreCase("")
                || otpThree.getText().toString().equalsIgnoreCase("")
                || otpFour.getText().toString().equalsIgnoreCase(""))
            validationMessage = getResources().getString(R.string.otp_verif_enter_otp);
        if (validationMessage.length() != 0) {
            //otpOne.setError(validationMessage);
            Util.makeToast(this, validationMessage);
        }
        return validationMessage.length() == 0;
    }

    @Click(R.id.change_otp_mobile)
    void changeMobile() {
        MobileNumberVarificationNew_.intent(this).start();
    }

    @Click(R.id.resend_code)
    void resendCode() {
        verifyMobile();
    }

    @Click(R.id.verify_otp)
    void verifiyOTP() {
        if (isOtpValid()) {
            confirmOtp();
        }
    }

    private void confirmOtp() {
        hideLoader(progress);
        showLoader(progress);
        String totalOTP = otpOne.getText().toString() + otpTwo.getText().toString() + otpThree.getText().toString() + otpFour.getText().toString();
        Call<APIResponse<User>> callback = apiService.confirmOtp(Constants.MB_API_KEY, mobile, totalOTP);
        callback.enqueue(new Callback<APIResponse<User>>() {
            @Override
            public void onResponse(Response<APIResponse<User>> response) {
                hideLoader(progress);
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().isStatus()) {
                            GoogleAnalyticEvents.event(OTPVerification.this, gaTracker, "Verification", "Guest user verified his number with MeraBreak", new Date().toString());
                           /* Map<String, Object> eventValue = new HashMap<String, Object>();
                            eventValue.put(AFInAppEventParameterName.VALIDATED, "Mobile Number Verified with MeraBreak");
                            ((ApplicationSingleton) OTPVerification.this.getApplication()).appsFlyerLib().trackEvent(OTPVerification.this, "Guest User Verification", eventValue);*/
                            // Util.setMobileVerified(OTPVerification.this);
                            User tempUser = response.body().getValues();
                            tempUser.setPhone(mobile);
                            Util.setUser(OTPVerification.this, tempUser);
                            if (tempUser.getFirstOpened() == 1)
                                Util.setFirstOpened(OTPVerification.this, "true");
                            if (tempUser.getReferOpened() == 1)
                                Util.setReferOpened(OTPVerification.this, "true");
                           // getDownloadedChallenges();
                            RegistrationActivityNew_.intent(OTPVerification.this).mobile(mobile).start();
                            finish();
                        } else {
                            Util.makeToast(OTPVerification.this, response.body().getMeta().getMessage());
                        }
                    } else {
                        Util.makeToast(OTPVerification.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(OTPVerification.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                Util.makeToast(OTPVerification.this, Constants.SOME_THING_WRONG);
            }
        });
    }

    private void getDownloadedChallenges() {
        user = Util.getUser(this);
        Call<APIResponse<List<DownloadedChallenges>>> callback = apiService.getDownChallenges(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<List<DownloadedChallenges>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<DownloadedChallenges>>> response) {
                if(response.code() == 200) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().isStatus()) {
                            if (response.body().getValues() != null && response.body().getValues().size() > 0) {
                                Util.saveDownChalSlugs(OTPVerification.this, response.body().getValues());
                                //Log.d("downchal res@Guest reg", new Gson().toJson(response.body().getValues()));
                            }
                        } else {
                            Util.makeToast(OTPVerification.this, response.body().getMeta().getMessage());
                        }
                    } else {
                        Util.makeToast(OTPVerification.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(OTPVerification.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void verifyMobile() {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse> callback = apiService.registerMobile(Constants.MB_API_KEY, mobile, Util.uniqueDeviceID(this));
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                hideLoader(progress);
                if(response.code() == 200 && response.body() != null){
                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().getStatusCode() == 200) {

                    } else if (response.body().getMeta().getStatusCode() == 203) {

                    } else if (response.body().getMeta().getStatusCode() == 409) {

                    } else {
                    }

                } else {
                    Util.makeToast(OTPVerification.this, Constants.SOME_THING_WRONG);
                }
                } else{
                    Util.makeToast(OTPVerification.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                if (t.getMessage() != null)
                    Log.d("## Mobile varification", t.getMessage());
            }
        });

    }

    @Subscribe
    public void otpReceived(OTPRecievedEvent otpRecievedEvent) {
        //Log.d("SmsReceiver otps", otpRecievedEvent.getOtp());
        otpOne.setText(otpRecievedEvent.getOtp().substring(0));
        otpTwo.setText(otpRecievedEvent.getOtp().substring(1));
        otpThree.setText(otpRecievedEvent.getOtp().substring(2));
        otpFour.setText(otpRecievedEvent.getOtp().substring(3));
        verifiyOTP();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.bus().unregister(this);
    }

    @TextChange(R.id.otp_one)
    void onOtpOne() {
        String otp = otpOne.getText().toString();
        if (otp.length() == 1) {
            otpTwo.requestFocus();
        }
    }

    @TextChange(R.id.otp_two)
    void onOtpTwo() {
        String otp = otpTwo.getText().toString();
        if (otp.length() == 1) {
            otpThree.requestFocus();
        }
    }

    @TextChange(R.id.otp_three)
    void onOtpThree() {
        String otp = otpThree.getText().toString();
        if (otp.length() == 1) {
            otpFour.requestFocus();
        }
    }
}
