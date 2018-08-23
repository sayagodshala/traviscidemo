package com.merabreak.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import com.google.gson.Gson;
import com.merabreak.ApplicationSingleton;
import com.merabreak.BaseActivity;
import com.merabreak.BusProvider;
import com.merabreak.Constants;
import com.merabreak.CustomListAdapter;
import com.merabreak.GoogleAnalyticEvents;
import com.merabreak.MainActivity;
import com.merabreak.MainActivity_;
import com.merabreak.OTPRecievedEvent;
import com.merabreak.R;
import com.merabreak.SearchedLocationItem;
import com.merabreak.SearchedLocationItem_;
import com.merabreak.Util;
import com.merabreak.models.APIResponse;
import com.merabreak.models.GooglePrediction;
import com.merabreak.models.ProfileDetails;
import com.merabreak.models.User;
import com.merabreak.network.GoogleAPIClient;
import com.merabreak.network.GoogleAPIResponse;
import com.merabreak.network.GoogleAPIService;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by Vinay on 8/26/2016.
 */
@EActivity(R.layout.auth_new)
public class RegistrationActivityNew extends BaseActivity {

    int mYear, mMonth, mDay;
    GoogleAPIService googleAPIService;
    private CustomListAdapter projectItemsAdapter = null;
    private String gender = "";

    @ViewById(R.id.male)
    Button male;

    @ViewById(R.id.female)
    Button female;

    @ViewById(R.id.dob_textview)
    TextView dobTv;

    @ViewById(R.id.location)
    EditText location;

    @ViewById(R.id.linear_list)
    LinearLayout linearList;

    @ViewById(R.id.list_views)
    ListView locationList;

    @ViewById(R.id.simple_text)
    TextView simpleLabel;

    @ViewById(R.id.signup_container)
    LinearLayout signupContainer;

    @ViewById(R.id.mandatory_fields_hldr)
    LinearLayout signupFeildsContainer;

    @ViewById(R.id.complete_profile_hldr)
    LinearLayout completeProfileHldr;

    @ViewById(R.id.progress)
    LinearLayout progress;

    @ViewById(R.id.full_name)
    MaterialEditText fullName;

    @ViewById(R.id.email)
    MaterialEditText email;

    @ViewById(R.id.password)
    MaterialEditText password;

    @ViewById(R.id.mobile)
    MaterialEditText mobileEdit;

    @ViewById(R.id.show_hide_pw)
    Switch shPW;

    @ViewById(R.id.show_hide)
    LinearLayout shHidePW;

    @ViewById(R.id.button_proceed)
    Button submit;

    @ViewById(R.id.otp_mobile)
    TextView enteredMobile;

    @ViewById(R.id.show_hide_text)
    TextView show_hide_text;

    @ViewById(R.id.otp_container)
    LinearLayout otpContainer;

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

    @ViewById(R.id.verifiy_otp)
    Button continueSignup;

    @ViewById(R.id.skip)
    TextView skip;

    @Extra
    User fbUser;

    @Extra
    String mobile = "";

    private String imagePath = "";
    private String userId;

    @AfterViews
    void init() {

        googleAPIService = GoogleAPIClient.getAPIService();
        BusProvider.bus().register(this);
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onSubmit();
                    return true;
                }
                return false;
            }
        });

        otpFour.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    verifiyOtp();
                    return true;
                }
                return false;
            }
        });

        if (fbUser != null) {
            if (fbUser.getEmail() != null) {
                email.setEnabled(false);
                email.setText(fbUser.getEmail());
            }
            fullName.setText(fbUser.getFullName());
            mobileEdit.requestFocus();
            imagePath = fbUser.getImage();
            userId = String.valueOf(fbUser.getUserId());
            password.setVisibility(View.GONE);
            shPW.setVisibility(View.GONE);
            shHidePW.setVisibility(View.GONE);
            skip.setVisibility(View.GONE);

        } else {
            mobileEdit.setVisibility(View.GONE);
        }

        dobTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                Date today = new Date();
                c.setTime(today);
                c.add( Calendar.YEAR, -5 ); // set 5 years as a minimum age limit of child

                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                long minDate = c.getTime().getTime();


                DatePickerDialog datePickerDialog = new DatePickerDialog(RegistrationActivityNew.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                                int dd = view.getDayOfMonth();
                                int mm = view.getMonth();
                                int yy = view.getYear();

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(yy,mm,dd);
                                String userDoB = format.format(calendar.getTime());

                                dobTv.setText(userDoB);

                            }
                        }, mYear, mMonth, mDay);


                datePickerDialog.getDatePicker().setMaxDate(minDate);
                datePickerDialog.show();
            }
        });
    }

    @Click(R.id.male)
    void onMale() {
        male.setSelected(true);
        female.setSelected(false);
        gender = "male";
    }

    @Click(R.id.female)
    void onFemale() {
        female.setSelected(true);
        male.setSelected(false);
        gender = "female";
    }

    @TextChange(R.id.location)
    void searchLocation() {
        if (location.getText().length() > 0)
            getGooglePlaces(location.getText().toString(), linearList, locationList);
        else
            linearList.setVisibility(View.GONE);

    }
    @ItemClick(R.id.list_views)
    void selectLocation(GooglePrediction item) {
        location.setText(item.getDescription());
        linearList.setVisibility(View.GONE);
    }

    public void getGooglePlaces(String text, LinearLayout linearList, ListView locationList) {
        Call<GoogleAPIResponse<List<GooglePrediction>>> callBack = null;
        try {
            callBack = googleAPIService.getGooglePlaces("false", "en", Constants.GOOGLE_APP_API_BROWSER_KEY, URLEncoder.encode(text, "utf8"), "country:in");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        callBack.enqueue(new Callback<GoogleAPIResponse<List<GooglePrediction>>>() {
            @Override
            public void onResponse(Response<GoogleAPIResponse<List<GooglePrediction>>> response) {
                Log.d("Retrofit Response", new Gson().toJson(response.body()));
                List<GooglePrediction> predictions = new ArrayList<GooglePrediction>();

                if (response.body() != null) {
                    if (response.body().getStatus().equalsIgnoreCase("ok")) {
                        predictions = response.body().getPredictions();
                        drawSearchedLocationList(predictions, linearList, locationList);
                    } else {
                        //Util.makeToast(MainActivity.this, "Not able to fetch location at this points of time.Please try again or enter manually");
                    }
                } else {
                    Util.makeToast(RegistrationActivityNew.this, "Not able to fetch location at this points of time.Please try again or enter manually");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (t != null)
                    Log.d("Retrofit Response", new Gson().toJson(t));
            }
        });
    }

    private void drawSearchedLocationList(List<GooglePrediction> values, LinearLayout linearList, ListView loc) {
        linearList.setVisibility(View.VISIBLE);
        projectItemsAdapter = new CustomListAdapter<>(values, this::searchedLocationItem);
        loc.setAdapter(projectItemsAdapter);
    }

    private SearchedLocationItem searchedLocationItem() {
        SearchedLocationItem item = SearchedLocationItem_.build(this);
        return item;
    }

   /* @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (fbUser != null || otpContainer.getVisibility() == View.VISIBLE) {
            finish();
        } else {
            onSkipButton();
        }
    }*/

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

    private boolean isFbSignUpValid() {
        String validationMessage = "";

        if (Util.textIsEmpty(fullName.getText().toString())) {
            validationMessage = getResources().getString(R.string.regitration_enter_name);
            fullName.setError(validationMessage);
        } else if (!Util.isValidEmail(email.getText().toString())) {
            validationMessage = getResources().getString(R.string.regitration_enter_valid_email);
            email.setError(getResources().getString(R.string.regitration_enter_valid_email));
        } else if (Util.textIsEmpty(mobileEdit.getText().toString()) || mobileEdit.getText().toString().length() < 10) {
            validationMessage = getResources().getString(R.string.regitration_enter_valid_mobile);
            mobileEdit.setError(validationMessage);
        }
        return validationMessage.length() == 0;
    }

    private boolean isManualSignUpValid() {
        String validationMessage = "";

        if (Util.textIsEmpty(fullName.getText().toString())) {
            validationMessage = getResources().getString(R.string.regitration_enter_name);
            fullName.setError(validationMessage);
        } else if (!Util.isValidEmail(email.getText().toString())) {
            validationMessage = getResources().getString(R.string.regitration_enter_valid_email);
            email.setError(getResources().getString(R.string.regitration_enter_valid_email));
        } else if (password.getText().length() < 6) {
            validationMessage = getResources().getString(R.string.regitration_enter_password);
            password.setError(getResources().getString(R.string.regitration_enter_password));
        } else if (Util.textIsEmpty(mobile)) {
            validationMessage = getResources().getString(R.string.regitration_enter_valid_mobile);
            Util.makeToast(this, validationMessage);
        }
        return validationMessage.length() == 0;
    }

    private boolean isOtpValid() {
        String validationMessage = "";
        if (otpOne.getText().toString().equalsIgnoreCase("")
                || otpTwo.getText().toString().equalsIgnoreCase("")
                || otpThree.getText().toString().equalsIgnoreCase("")
                || otpFour.getText().toString().equalsIgnoreCase(""))
            validationMessage = getResources().getString(R.string.regitration_enter_otp);
        if (validationMessage.length() != 0) {
            Util.makeToast(this, validationMessage);
        }
        return validationMessage.length() == 0;
    }

    @Click(R.id.button_proceed)
    void onSubmit() {
        if (fbUser != null) {
            if (isFbSignUpValid()) {
                authSocialSignup();
            }
        } else if (isManualSignUpValid()) {
            signup();
        }
    }

    @Click(R.id.verifiy_otp)
    void verifiyOtp() {
        if (isOtpValid()) {
            confirmOtp();
        }
    }

    @Click(R.id.resend_code)
    void resendOtp() {
        verifyMobile();
    }

    @Click(R.id.button_register)
    void updateAddtionalInfo()
    {
        if(isAdditionalInfoValidate(gender,dobTv.getText().toString(),location.getText().toString())){
            postProfileDetails(gender.toLowerCase(), dobTv.getText().toString(), location.toString().trim());
        }
    }

    @Click(R.id.button_skip)
    void skipUpdateInfo()
    {
        MainActivity_.intent(RegistrationActivityNew.this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK).start();
        finish();
    }

    private boolean isAdditionalInfoValidate(String dob, String gender, String location){

        if (Util.textIsEmpty(location.toString().trim())) {
            Util.makeToast(RegistrationActivityNew.this, "Please enter your location");
            return false;
        }
        else if (Util.textIsEmpty(gender.toString().trim())) {
            //postProfileDetails(gender, "", "", genderSelection, dob, linearList, location, profileUpdate, maleImage, femaleImage, profileProgress, progressPercentage, or, skip, date, month, year, userLocation, locationList);
            Util.makeToast(RegistrationActivityNew.this, "Please select gender");
            return false;
        }
        else if (Util.textIsEmpty(dob.toString().trim())) {

            Util.makeToast(RegistrationActivityNew.this, "Please choose your date of birth");
            return false;
        }
        else
        return true;

    }

    public void postProfileDetails(String updatedGender, String updatedDob, String updatedLocation) {
        Call<APIResponse<ProfileDetails>> callback = apiService.postProfileDetails(Constants.MB_API_KEY, Util.getUser(RegistrationActivityNew.this).getAuthToken(), updatedGender, updatedDob, updatedLocation);
        callback.enqueue(new Callback<APIResponse<ProfileDetails>>() {
            @Override
            public void onResponse(Response<APIResponse<ProfileDetails>> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta().statusCode == 200) {
                        int profilePertcentage = response.body().getValues().getProfilePercentage();
                        int genderFlag = response.body().getValues().getGenderFlag();
                        int dobFlag = response.body().getValues().getDobFlag();
                        int locationFlag = response.body().getValues().getLocationFlag();

                        if(dobFlag == 1) {
                            user.setDob(dobTv.getText().toString().trim());
                        }
                        if(genderFlag == 1) {
                            user.setGender(gender.toLowerCase());
                        }
                        if(locationFlag == 1) {
                            user.setLocation(location.getText().toString().toLowerCase().trim());
                        }

                        Util.makeToast(RegistrationActivityNew.this, response.body().getMeta().getMessage());

                        MainActivity_.intent(RegistrationActivityNew.this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK).start();
                        finish();
                    }
                } else {
                    Util.makeToast(RegistrationActivityNew.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
            }
        });
    }


    private void authSocialSignup() {
        hideLoader(progress);
        showLoader(progress);
        String referrer = Util.getCampaignReferrer(context);
        String promo_code = Util.getCampaignName(context);
        enteredMobile.setText(mobileEdit.getText().toString());
        Call<APIResponse<User>> callBack = apiService.newAuthSocialSignup(Constants.MB_API_KEY,
                fullName.getText().toString().trim(),
                email.getText().toString().trim(),
                mobileEdit.getText().toString().trim(),
                imagePath, userId, referrer, promo_code);
        callBack.enqueue(new Callback<APIResponse<User>>() {
            @Override
            public void onResponse(Response<APIResponse<User>> response) {
                hideLoader(progress);
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 200) {
                            fbUser = null;
                            otpContainer.setVisibility(View.VISIBLE);
                            skip.setVisibility(View.GONE);
                            signupContainer.setVisibility(View.GONE);
                            simpleLabel.setText(R.string.verify_otp_fb);
                        } else if (response.body().getMeta().getStatusCode() == 409) {
                            mobileEdit.requestFocus();
                            Util.makeToast(RegistrationActivityNew.this, response.body().getMeta().getMessage());
                        } else if (response.body().getMeta().getStatusCode() == 100) {
                            if (!referrer.equalsIgnoreCase("")) {
                                GoogleAnalyticEvents.eventCampaign(context, gaTracker, "Campaign", "User Regd with Fb through Campaign", referrer, mobileEdit.getText().toString());
                                Util.saveCampaignReferrer(RegistrationActivityNew.this, "");
                            } else {
                                GoogleAnalyticEvents.eventSignup(RegistrationActivityNew.this, gaTracker, "FB SignUp", "User SignUp for MeraBreak with FB", mobileEdit.getText().toString());
                            }
                            Util.saveCampaignName(RegistrationActivityNew.this, "");
                           /* Map<String, Object> eventValue = new HashMap<String, Object>();
                            eventValue.put(AFInAppEventParameterName.REGSITRATION_METHOD, "User SignUp for MeraBreak with FB");
                            ((ApplicationSingleton) RegistrationActivityNew.this.getApplication()).appsFlyerLib().trackEvent(RegistrationActivityNew.this, "FB SignUp", eventValue);*/
                            User newUser = response.body().getValues();
                            newUser.setPhone(mobileEdit.getText().toString());
                            Util.setMobileVerified(RegistrationActivityNew.this);
                            Util.setUser(RegistrationActivityNew.this, response.body().getValues());
                            Util.setUserLogin(RegistrationActivityNew.this);
                            if (newUser.getFirstOpened() == 1)
                                Util.setFirstOpened(RegistrationActivityNew.this, "true");
                            if (newUser.getReferOpened() == 1)
                                Util.setReferOpened(RegistrationActivityNew.this, "true");
                            MainActivity_.intent(RegistrationActivityNew.this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK).start();
                            finish();
                            //gotoMainScreen(newUser);
                        }
                    } else {
                        Util.makeToast(RegistrationActivityNew.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(RegistrationActivityNew.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                Util.makeToast(RegistrationActivityNew.this, Constants.SOME_THING_WRONG);
            }
        });
    }

    private void confirmOtp() {
        hideLoader(progress);
        showLoader(progress);
        String totalOTP = otpOne.getText().toString() + otpTwo.getText().toString() + otpThree.getText().toString() + otpFour.getText().toString();
        Call<APIResponse<User>> callback = apiService.socialConfirmOtp(Constants.MB_API_KEY, enteredMobile.getText().toString().trim(), totalOTP);
        callback.enqueue(new Callback<APIResponse<User>>() {
            @Override
            public void onResponse(Response<APIResponse<User>> response) {
                hideLoader(progress);
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().isStatus()) {
                            User newUser = response.body().getValues();
                            newUser.setPhone(enteredMobile.getText().toString());
                            Util.setMobileVerified(RegistrationActivityNew.this);
                            Util.setUser(RegistrationActivityNew.this, response.body().getValues());
                            Util.setUserLogin(RegistrationActivityNew.this);
                            if (newUser.getFirstOpened() == 1)
                                Util.setFirstOpened(RegistrationActivityNew.this, "true");
                            if (newUser.getReferOpened() == 1)
                                Util.setReferOpened(RegistrationActivityNew.this, "true");
                            MainActivity_.intent(RegistrationActivityNew.this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK).start();
                            finish();
                        } else {
                            Util.makeToast(RegistrationActivityNew.this, response.body().getMeta().getMessage());
                        }
                    } else {
                        Util.makeToast(RegistrationActivityNew.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(RegistrationActivityNew.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                Util.makeToast(RegistrationActivityNew.this, Constants.SOME_THING_WRONG);
            }
        });
    }

    private void signup() {
        hideLoader(progress);
        showLoader(progress);
        String referrer = Util.getCampaignReferrer(context);
        String promo_code = Util.getCampaignName(context);
        Call<APIResponse<User>> callback = apiService.newSignup(Constants.MB_API_KEY,
                String.valueOf(user.getUserId()),
                fullName.getText().toString().trim(),
                email.getText().toString().trim(),
                mobile,
                password.getText().toString(),
                "", "", referrer, promo_code);
        callback.enqueue(new Callback<APIResponse<User>>() {
            @Override
            public void onResponse(Response<APIResponse<User>> response) {
                hideLoader(progress);
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().isStatus()) {
                            if (!referrer.equalsIgnoreCase("")) {
                                GoogleAnalyticEvents.eventCampaign(context, gaTracker, "Campaign", "User Regd through Campaign", referrer, mobile);
                                Util.saveCampaignReferrer(RegistrationActivityNew.this, "");
                            } else {
                                GoogleAnalyticEvents.eventSignup(RegistrationActivityNew.this, gaTracker, "SignUp", "User SignUp for MeraBreak", fullName.getText().toString().trim() + " " + mobile);
                            }
                            Util.saveCampaignName(RegistrationActivityNew.this, "");
                            /* Map<String, Object> eventValue = new HashMap<String, Object>();
                            eventValue.put(AFInAppEventParameterName.REGSITRATION_METHOD, "MeraBreak Signup");
                            ((ApplicationSingleton) RegistrationActivityNew.this.getApplication()).appsFlyerLib().trackEvent(RegistrationActivityNew.this, "SignUp", eventValue);*/
                            User user = response.body().getValues();
                            user.setAccountType("merabreak");
                            Util.setUser(RegistrationActivityNew.this, user);
                            Util.setUserLogin(RegistrationActivityNew.this);
                            if (user.getFirstOpened() == 1)
                                Util.setFirstOpened(RegistrationActivityNew.this, "true");
                            if (user.getReferOpened() == 1)
                                Util.setReferOpened(RegistrationActivityNew.this, "true");
                            MainActivity_.intent(RegistrationActivityNew.this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK).start();
                            finish();

                            //gotoMainScreen(user);
                        } else {
                            Util.makeToast(RegistrationActivityNew.this, response.body().getMeta().getMessage());
                        }
                    } else {
                        Util.makeToast(RegistrationActivityNew.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(RegistrationActivityNew.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                Util.makeToast(RegistrationActivityNew.this, Constants.SOME_THING_WRONG);
            }
        });
    }

    private void gotoMainScreen(User user)
    {
        //if(user.getDob() == null || user.getDob().isEmpty())
        if(user.getLocation() == null || user.getLocation().isEmpty())
        {
            signupFeildsContainer.setVisibility(View.GONE);
            completeProfileHldr.setVisibility(View.VISIBLE);

        }
        else
        {
            MainActivity_.intent(RegistrationActivityNew.this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK).start();
            finish();
        }
    }


    private void verifyMobile() {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse> callback = apiService.registerMobile(Constants.MB_API_KEY, enteredMobile.getText().toString(), Util.uniqueDeviceID(this));
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
                    Util.makeToast(RegistrationActivityNew.this, Constants.SOME_THING_WRONG);
                }
                } else {
                   Util.makeToast(RegistrationActivityNew.this, Constants.SOME_THING_WRONG);
               }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                Util.makeToast(RegistrationActivityNew.this, Constants.SOME_THING_WRONG);
            }
        });
    }

    @Subscribe
    public void otpReceived(OTPRecievedEvent otpRecievedEvent) {
        otpOne.setText(otpRecievedEvent.getOtp().substring(0));
        otpTwo.setText(otpRecievedEvent.getOtp().substring(1));
        otpThree.setText(otpRecievedEvent.getOtp().substring(2));
        otpFour.setText(otpRecievedEvent.getOtp().substring(3));
        confirmOtp();
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

    @Click(R.id.skip)
    void onSkip() {
        onSkipButton();
    }

    private void onSkipButton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.regitration_skip)).setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showLoader();
                GoogleAnalyticEvents.eventSignup(RegistrationActivityNew.this, gaTracker, "SignUp Skip", "User Skipped SignUp", new Date().toString());
               /* Map<String, Object> eventValue = new HashMap<String, Object>();
                eventValue.put(AFInAppEventParameterName.PARAM_2, "User Skipped Signup");
                ((ApplicationSingleton) RegistrationActivityNew.this.getApplication()).appsFlyerLib().trackEvent(RegistrationActivityNew.this, "SignUp Skip", eventValue);*/
                MainActivity_.intent(RegistrationActivityNew.this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK).start();
                finish();
            }
        }).setNegativeButton(getResources().getString(R.string.no), null)                        //Do nothing on no
                .show();
    }
}
