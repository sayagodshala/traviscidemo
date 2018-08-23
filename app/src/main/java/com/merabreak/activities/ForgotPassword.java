package com.merabreak.activities;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.merabreak.BaseActivity;
import com.merabreak.BusProvider;
import com.merabreak.Constants;
import com.merabreak.ForgotPasswordEvent;
import com.merabreak.OTPRecievedEvent;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.imageloader.ImageLoader;
import com.merabreak.models.APIResponse;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by Ewebcore on 14-Jan-16.
 */
@EActivity(R.layout.forgot_password)
public class ForgotPassword extends BaseActivity {

    private ImageLoader mImageLoader;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.mobile)
    EditText mobile;
    @ViewById(R.id.otp)
    EditText otp;
    @ViewById(R.id.password)
    EditText password;
    @ViewById(R.id.confirm_password)
    EditText confirmPassword;

    @Extra
    String mobileNumber = "";

    @AfterViews
    void init() {
        BusProvider.bus().register(this);
        mImageLoader = new ImageLoader(this);
        title.setText("Forgot Password");
        toolbar.setNavigationIcon(R.drawable.button_back1);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());
        mobile.setText(mobileNumber);
        generateOTPView();
    }

    private void generateOTPView() {
        otp.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        confirmPassword.setVisibility(View.GONE);
    }

    private void changePasswordView() {
        otp.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        confirmPassword.setVisibility(View.VISIBLE);
        mobile.setEnabled(false);
    }

    @Click(R.id.submit)
    void onSubmit() {

        if (mobile.isEnabled()) {
            if (mobile.getText().toString().length() >= 10) {
                forgotPasswordConfirmationCode();
            } else {
                mobile.setError("Please enter mobile");
            }
        } else {
            if (isSignUpValid()) {
                forgotPasswordUpdate();
            }
        }
    }

    private boolean isSignUpValid() {
        String validationMessage = "";
        if (otp.getText().toString().equalsIgnoreCase("")) {
            validationMessage = "Please enter OTP";
            otp.setError(validationMessage);
        } else if (password.getText().length() < 8) {
            validationMessage = "Password must be 8 characters long or above";
            password.setError("Password must be 8 characters long");
        } else if (!confirmPassword.getText().toString().equalsIgnoreCase(password.getText().toString().toLowerCase())) {
            validationMessage = "Confirm Password not matched";
            confirmPassword.setError("Confirm Password not matched");
        }


//        if (validationMessage.length() != 0) {
//            Util.makeToast(this, validationMessage);
//        }

        return validationMessage.length() == 0;
    }

    private void forgotPasswordConfirmationCode() {
        hideLoader();
        showLoader();
        Call<APIResponse> callback = apiService.forgotPasswordConfirmationCode(Constants.MB_API_KEY, mobile.getText().toString());
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                hideLoader();
                if (response.body().getMeta() != null) {
                    Util.makeToast(ForgotPassword.this, response.body().getMeta().getMessage());
                    if (response.body().getMeta().isStatus()) {
                        changePasswordView();
                    }
                } else {
                    Util.makeToast(ForgotPassword.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                if(t.getMessage()!=null) {
                    Log.d("JSONError", t.getMessage());
                    Util.makeToast(ForgotPassword.this, t.getMessage());
                }else {
                    Log.d("API Error", Constants.SOME_THING_WRONG);
                }

            }
        });
    }

    private void forgotPasswordUpdate() {
        hideLoader();
        showLoader();
        Call<APIResponse> callback = apiService.forgotPasswordUpdate(Constants.MB_API_KEY, otp.getText().toString(), mobile.getText().toString(), password.getText().toString());
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                hideLoader();
                if (response.body().getMeta() != null) {
                    Util.makeToast(ForgotPassword.this, response.body().getMeta().getMessage());
                    if (response.body().getMeta().isStatus()) {
                        BusProvider.bus().post(new ForgotPasswordEvent(mobile.getText().toString()));
                        finish();
                    }
                } else {
                    Util.makeToast(ForgotPassword.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                if(t.getMessage()!=null) {
                    Log.d("JSONError", t.getMessage());
                    Util.makeToast(ForgotPassword.this, t.getMessage());
                }else {
                    Log.d("API Error", Constants.SOME_THING_WRONG);
                }
            }
        });
    }

    @Subscribe
    public void otpReceived(OTPRecievedEvent otpRecievedEvent) {
        //Log.d("SmsReceiver", otpRecievedEvent.getOtp());
        otp.setText(otpRecievedEvent.getOtp());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.bus().unregister(this);
    }


}
