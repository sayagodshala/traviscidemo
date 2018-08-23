package com.merabreak.activities;

import android.content.Intent;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.merabreak.BaseActivity;
import com.merabreak.BusProvider;
import com.merabreak.Constants;
import com.merabreak.ForgotPasswordEvent;
import com.merabreak.OTPRecievedEvent;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.models.APIResponse;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by Vinay on 8/25/2016.
 */
@EActivity(R.layout.forgot_password_new)
public class ForgotPasswordNew extends BaseActivity {

    @Extra
    String mobile;

    @ViewById(R.id.otp_mobile)
    TextView enteredMob;

    @ViewById(R.id.entered_mob)
    TextView enteredMob1;

    @ViewById(R.id.progress)
    LinearLayout progress;

    @ViewById(R.id.label)
    TextView label;

    @ViewById(R.id.show_hide_text)
    TextView show_hide_text;

    @ViewById(R.id.password)
    MaterialEditText password;

    @ViewById(R.id.show_hide_pw)
    Switch shPW;

    @ViewById(R.id.otp_one)
    EditText otpOne;

    @ViewById(R.id.otp_two)
    EditText otpTwo;

    @ViewById(R.id.otp_three)
    EditText otpThree;

    @ViewById(R.id.otp_four)
    EditText otpFour;

    @ViewById(R.id.resend_code)
    LinearLayout resendOtp;

    @ViewById(R.id.container_new_password)
    LinearLayout container_new_password;

    @ViewById(R.id.otp_container)
    LinearLayout otp_container;

    String totalOTP;

    @AfterViews
    void init() {
        BusProvider.bus().register(this);
        enteredMob.setText(mobile);
        enteredMob1.setText(mobile);
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onSubmitPW();
                    return true;
                }
                return false;
            }
        });

        forgotPasswordConfirmationCode();
    }

    @CheckedChange(R.id.show_hide_pw)
    void showHidePW() {
        if (shPW.isChecked()) {
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            show_hide_text.setText(R.string.fogot_pw_hide);
        } else {
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            show_hide_text.setText(R.string.fogot_pw_show);
        }
    }

    @Click(R.id.verify_otp)
    void onSubmit() {
        String validationMessage = "";
        if (otpOne.getText().toString().equalsIgnoreCase("")
                && otpTwo.getText().toString().equalsIgnoreCase("")
                && otpThree.getText().toString().equalsIgnoreCase("")
                && otpFour.getText().toString().equalsIgnoreCase("")) {
            validationMessage = getResources().getString(R.string.fogot_pw_enter_otp);
        } else {
            forgotPasswordUpdate();
        }
    }

    @Click(R.id.submit)
    void onSubmitPW() {
        String validationMessage = "";
        if (password.getText().length() < 6) {
            validationMessage = getResources().getString(R.string.fogot_pw_six_char);
            password.setError(getResources().getString(R.string.fogot_pw_six_char));
        } else {
            newPasswordUpdate();
        }
    }

    @Click(R.id.resend_code)
    void resendOtp() {
        forgotPasswordConfirmationCode();
    }

    @Click(R.id.change_otp_mobile)
    void changeMobile() {
        MobileNumberVarificationNew_.intent(ForgotPasswordNew.this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK).start();
    }

    private void newPasswordUpdate() {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse> callback = apiService.forgotPasswordUpdate(Constants.MB_API_KEY, totalOTP, mobile, password.getText().toString());
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                hideLoader(progress);
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        //  Util.makeToast(ForgotPasswordNew.this, response.body().getMeta().getMessage());
                        if (response.body().getMeta().isStatus()) {
                            BusProvider.bus().post(new ForgotPasswordEvent(mobile));
                            finish();
                        } else {
                            Util.makeToast(ForgotPasswordNew.this, response.body().getMeta().getMessage());
                        }
                    } else {
                        Util.makeToast(ForgotPasswordNew.this, Constants.SOME_THING_WRONG);
                    }
                }else {
                    Util.makeToast(ForgotPasswordNew.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                if (t.getMessage() != null) {
                    Log.d("JSONError", t.getMessage());
                    Util.makeToast(ForgotPasswordNew.this, t.getMessage());
                } else {
                    Log.d("API Error", Constants.SOME_THING_WRONG);
                }
            }
        });
    }

    private void forgotPasswordUpdate() {
        hideLoader(progress);
        showLoader(progress);
        totalOTP = otpOne.getText().toString() + otpTwo.getText().toString() + otpThree.getText().toString() + otpFour.getText().toString();
        Call<APIResponse> callback = apiService.forgotPassword(Constants.MB_API_KEY, mobile, totalOTP);
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                hideLoader(progress);
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().isStatus()) {
                            // Util.makeToast(ForgotPasswordNew.this, response.body().getMeta().getMessage());
                            otp_container.setVisibility(View.GONE);
                            container_new_password.setVisibility(View.VISIBLE);
                            label.setText(R.string.new_pass);
                        } else {
                            Util.makeToast(ForgotPasswordNew.this, response.body().getMeta().getMessage());
                        }
                    } else {
                        Util.makeToast(ForgotPasswordNew.this, Constants.SOME_THING_WRONG);
                    }
                }else {
                    Util.makeToast(ForgotPasswordNew.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                if (t.getMessage() != null) {
                    Log.d("JSONError", t.getMessage());
                    Util.makeToast(ForgotPasswordNew.this, t.getMessage());
                } else {
                    Log.d("API Error", Constants.SOME_THING_WRONG);
                }
            }
        });
    }

    private void forgotPasswordConfirmationCode() {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse> callback = apiService.forgotPasswordConfirmationCode(Constants.MB_API_KEY, mobile);
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                hideLoader(progress);
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        //  Util.makeToast(ForgotPasswordNew.this, response.body().getMeta().getMessage());
                        if (response.body().getMeta().isStatus()) {
                        }
                    } else {
                        Util.makeToast(ForgotPasswordNew.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(ForgotPasswordNew.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                    Log.d("API Error", Constants.SOME_THING_WRONG);

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
        onSubmit();
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
