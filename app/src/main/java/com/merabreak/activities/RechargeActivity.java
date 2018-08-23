package com.merabreak.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merabreak.AmountCoinsEvent;
import com.merabreak.BaseActivity;
import com.merabreak.BusProvider;
import com.merabreak.CircleDailog;
import com.merabreak.CircleDailog_;
import com.merabreak.CircleRecievedEvent;
import com.merabreak.Constants;
import com.merabreak.MainActivity_;
import com.merabreak.OperatorDialog;
import com.merabreak.OperatorDialog_;
import com.merabreak.OpratorRecievedEvent;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.models.APIResponse;
import com.merabreak.models.AccountDetails;
import com.merabreak.models.Circles;
import com.merabreak.models.HomeRechargePlans;
import com.merabreak.models.MobileDetails;
import com.merabreak.models.Operators;
import com.merabreak.models.RechargePlans;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by Vinay on 7/26/2016.
 */
@EActivity(R.layout.recharge_mobile)
public class RechargeActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.pointsNum)
    TextView pointsNum;

    @ViewById(R.id.progress)
    LinearLayout progress;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.mobile_number)
    MaterialEditText mobileNumber;

    @ViewById(R.id.operator)
    MaterialEditText operator;

    @ViewById(R.id.circle)
    MaterialEditText circle;

    @ViewById(R.id.amount)
    MaterialEditText amountForRecharge;

    @ViewById(R.id.coins_for_recharge)
    MaterialEditText coinsForRecharge;

    @ViewById(R.id.show_plans)
    Button showPlans;

    @ViewById(R.id.proceed_recharge)
    Button ProceedRecharge;

    @ViewById(R.id.change_plan)
    Button changePlan;

    @ViewById(R.id.recherge_container)
    RelativeLayout rechargeContainer;

    @ViewById(R.id.contacts)
    ImageView contacts;

    AlertDialog alertDialog;

    @Extra
    RechargePlans rechargePlans;

    String amount, coins , tabName;

    public static final int REQUEST_SELECT_CONTACT = 1001;

    private static final int MY_PERMISSION_CONTACTS = 101;

    @AfterViews
    void init() {
        title.setText(R.string.recharge_title);
        toolbar.setNavigationIcon(R.drawable.mb_back_icon);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());

        mobileNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    showPlans();
                    return true;
                }
                return false;
            }
        });
    }

    @AfterViews
    void registerBusProvider() {
        BusProvider.bus().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        AccountDetails ad = Util.getUserAccountDetails(getApplicationContext());
        if (ad != null)
            pointsNum.setText(doubleToStringNoDecimal(ad.getCoinBalance()));
    }

    public static String doubleToStringNoDecimal(double d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###");
        return formatter.format(d);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.bus().unregister(this);
    }

    private void getOperatorAndCircle() {
        Call<APIResponse<MobileDetails>> callback = apiService.getOperatorAndCircle(Constants.MB_API_KEY, user.getAuthToken(), mobileNumber.getText().toString());
        callback.enqueue(new Callback<APIResponse<MobileDetails>>() {
            @Override
            public void onResponse(Response<APIResponse<MobileDetails>> response) {
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(RechargeActivity.this, user);
                                getOperatorAndCircle();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else if (response.body().getMeta().getStatusCode() == 404) {
                            operator.setText("");
                            circle.setText("");
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                if (response.body().getValues() != null) {
                                    operator.setText(response.body().getValues().getOperator_name());
                                    operator.setTag(response.body().getValues().getOperator_code());
                                    circle.setText(response.body().getValues().getCircle_name());
                                    circle.setTag(response.body().getValues().getCircle_code());
                                } else {
                                    Util.makeToast(context, response.body().getMeta().getMessage());
                                }
                            } else {
                                Util.makeToast(context, response.body().getMeta().getMessage());
                            }
                        }
                    } else {
                        Util.makeToast(context, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(context, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Util.makeToast(context, Constants.SOME_THING_WRONG);
            }
        });
    }

    @Click(R.id.operator)
    void selectOperator() {
        getOperatorList();
    }

    private void getOperatorList() {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse<List<Operators>>> callback = apiService.getOperatorList(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<List<Operators>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Operators>>> response) {
                hideLoader(progress);
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(RechargeActivity.this, user);
                                getOperatorList();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                if (response.body().getValues() != null && response.body().getValues().size() > 0) {
                                    showSelectPlansDialog((ArrayList<Operators>) response.body().getValues());

                                } else {
                                    Util.makeToast(context, response.body().getMeta().getMessage());
                                }
                            } else {
                                Util.makeToast(context, response.body().getMeta().getMessage());
                            }
                        }
                    } else {
                        Util.makeToast(context, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(context, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
            }
        });
    }

    private void showSelectPlansDialog(ArrayList<Operators> list) {

        OperatorDialog_.builder().arg("MOBILE_OPERATOR", list).build()
                .show(getSupportFragmentManager(), OperatorDialog.class.getSimpleName());
    }

    @Click(R.id.circle)
    void selectCircle() {
        getCircleList();
    }

    private void getCircleList() {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse<List<Circles>>> callback = apiService.getCircleList(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<List<Circles>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Circles>>> response) {
                hideLoader(progress);
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(RechargeActivity.this, user);
                                getCircleList();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                if (response.body().getValues() != null && response.body().getValues().size() > 0) {
                                    showSelectCirclesDialog((ArrayList<Circles>) response.body().getValues());

                                } else {
                                    Util.makeToast(context, response.body().getMeta().getMessage());
                                }
                            } else {
                                Util.makeToast(context, response.body().getMeta().getMessage());
                            }
                        }
                    } else {
                        Util.makeToast(context, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(context, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
            }
        });
    }

    private void showSelectCirclesDialog(ArrayList<Circles> list) {
        CircleDailog_.builder().arg("MOBILE_CIRCLE", list).build()
                .show(getSupportFragmentManager(), CircleDailog.class.getSimpleName());
    }

    @Click(R.id.show_plans)
    void showPlans() {
        if (isValid()) {
            getRechargePlans();
        }
    }

    @Click(R.id.change_plan)
    void changePlans() {
        if (isValid()) {
            getRechargePlans();
        }
    }

    @Click(R.id.contacts)
    void getContacts() {
        requestPermissions();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void requestPermissions() {
        if (!hasPermissions(this, android.Manifest.permission.READ_CONTACTS)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_CONTACTS)) {
                appAlert();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_CONTACTS},
                        MY_PERMISSION_CONTACTS);
            }
        } else {
            openContact();
        }
    }

    private void appAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.recharge_perm_app_alert));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(RechargeActivity.this,
                        new String[]{android.Manifest.permission.READ_CONTACTS},
                        MY_PERMISSION_CONTACTS);
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(getResources().getColor(R.color.selected_tab_color));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openContact();
                } else {
                    Util.makeToast(this, getResources().getString(R.string.recharge_perm_toast));
                }
                return;
            }
        }
    }

    private void openContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_SELECT_CONTACT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_CONTACT) {
            if (resultCode == RESULT_OK) {
                Uri contactUri = data.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(column);
                if (number.startsWith("0")) {
                    number = number.replaceFirst("0", "");
                }
                String filteredNum = number.replaceAll("[\\-\\ ]", "").replace("+91", "");
                mobileNumber.setText(filteredNum);
            }
        }
    }

    private void getRechargePlans() {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse<List<HomeRechargePlans>>> callback = apiService.getAllRechargePlans(Constants.MB_API_KEY, user.getAuthToken(),
                operator.getTag().toString(),
                circle.getTag().toString());
        callback.enqueue(new Callback<APIResponse<List<HomeRechargePlans>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<HomeRechargePlans>>> response) {
                hideLoader(progress);
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(RechargeActivity.this, user);
                                getRechargePlans();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                if (response.body().getValues() != null && response.body().getValues().size() > 0) {
                                    Util.savePlans(RechargeActivity.this, response.body().getValues());
                                    RechargeDetailsActivity_.intent(RechargeActivity.this).start();
                                } else {
                                    Util.makeToast(context, response.body().getMeta().getMessage());
                                }
                            } else {
                                Util.makeToast(context, response.body().getMeta().getMessage());
                            }
                        }
                    } else {
                        Util.makeToast(context, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(context, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
            }
        });
    }

    @Click(R.id.proceed_recharge)
    void rechargeMobile() {
        makeRecharge();
    }

    private void makeRecharge() {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse> callback = apiService.getRecharge(Constants.MB_API_KEY, user.getAuthToken(),
                operator.getTag().toString(),
                mobileNumber.getText().toString(),
                amountForRecharge.getText().toString(),
                circle.getTag().toString(), tabName);
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                hideLoader(progress);
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(RechargeActivity.this, user);
                                makeRecharge();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else if (response.body().getMeta().getStatusCode() == 200) {
                            if (response.body().getMeta().isStatus()) {
                              //  Util.makeToast(context, response.body().getMeta().getMessage());
                               // MainActivity_.intent(context).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).flags(Intent.FLAG_ACTIVITY_NEW_TASK).startBtn();
                                rechargeDone(response.body().getMeta().getMessage());
                            } else {
                                Util.makeToast(context, response.body().getMeta().getMessage());
                            }
                        } else {
                            Util.makeToast(context, response.body().getMeta().getMessage());
                        }
                    } else {
                        Util.makeToast(context, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(context, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
            }
        });
    }

    @Subscribe
    public void operatorRecievedEvent(OpratorRecievedEvent event) {
        operator.setText(event.getOperators().getName());
        operator.setTag(event.getOperators().getCode());
    }

    @Subscribe
    public void circleRecievedEvent(CircleRecievedEvent event) {
        circle.setText(event.getCircles().getName());
        circle.setTag(event.getCircles().getCode());
    }
    @Subscribe
    public void setRecharPlanData(AmountCoinsEvent amountCoinsEvent) {
        if (amountCoinsEvent != null && amountCoinsEvent.getAmount() != null && amountCoinsEvent.getCoins() != null) {
            //Log.d("called", "called");
            amountForRecharge.setVisibility(View.VISIBLE);
            coinsForRecharge.setVisibility(View.VISIBLE);
            amountForRecharge.setText(amountCoinsEvent.getAmount());
            coinsForRecharge.setText(amountCoinsEvent.getCoins());
            tabName = amountCoinsEvent.getTabName();
            rechargeContainer.setVisibility(View.VISIBLE);
            contacts.setVisibility(View.GONE);
            showPlans.setVisibility(View.GONE);

        } else {
            amountForRecharge.setVisibility(View.GONE);
            coinsForRecharge.setVisibility(View.GONE);
            rechargeContainer.setVisibility(View.GONE);
            showPlans.setVisibility(View.VISIBLE);
            contacts.setVisibility(View.VISIBLE);
        }
    }

    private boolean isValid() {

        String validationMessage = "";
        if (Util.textIsEmpty(mobileNumber.getText().toString().trim())) {
            validationMessage = getResources().getString(R.string.recharge_no_mobile);
        } else if (mobileNumber.getText().toString().trim().length() < 10) {
            validationMessage = getResources().getString(R.string.recharge_mobile_not_valid);
        } else if (Util.textIsEmpty(operator.getText().toString().trim())) {
            validationMessage = getResources().getString(R.string.recharge_no_operator);
        } else if (Util.textIsEmpty(circle.getText().toString().trim())) {
            validationMessage = getResources().getString(R.string.recharge_no_circle);
        }
        if (!validationMessage.equalsIgnoreCase("")) {
            Util.makeToast(context, validationMessage);
        }
        return validationMessage.length() == 0;
    }

    @TextChange(R.id.mobile_number)
    void onTextChange() {
        String mobile = mobileNumber.getText().toString();
        if (!Util.textIsEmpty(mobile)) {
            if (mobile.length() == 4) {
                getOperatorAndCircle();
            }
            if (mobile.length() < 10) {
                // operator.setText("");
                // circle.setText("");
                amountForRecharge.setVisibility(View.GONE);
                coinsForRecharge.setVisibility(View.GONE);
                rechargeContainer.setVisibility(View.GONE);
                contacts.setVisibility(View.VISIBLE);
                showPlans.setVisibility(View.VISIBLE);
            }
        } else {
            operator.setText("");
            circle.setText("");
            amountForRecharge.setVisibility(View.GONE);
            coinsForRecharge.setVisibility(View.GONE);
            rechargeContainer.setVisibility(View.GONE);
            showPlans.setVisibility(View.VISIBLE);
        }
    }

    private void rechargeDone(String msg){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.app_update_dialog, null);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/roboto_medium.ttf");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView title = (TextView) promptView.findViewById(R.id.dialog_title);
        TextView desc = (TextView) promptView.findViewById(R.id.dialog_desc);
        Button appUpdate = (Button) promptView.findViewById(R.id.dialog_action_btn);
        ImageView gifView = (ImageView) promptView.findViewById(R.id.imageViewOverlay);
        title.setTypeface(font);
        title.setText("There you go!");
        desc.setText(msg);
        appUpdate.setText("Okay");
        gifView.setImageDrawable(getResources().getDrawable(R.drawable.recharge_icon));
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        appUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity_.intent(context).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(promptView);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }
}
