package com.merabreak.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.models.APIResponse;
import com.merabreak.models.ShippingAddress;
import com.merabreak.models.SimpleObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by Ewebcore on 05-Feb-16.
 */
@EActivity(R.layout.shipping_address)
public class ShippingAddressActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.full_name)
    EditText fullName;

    @ViewById(R.id.mobile)
    EditText mobile;

    @ViewById(R.id.alternate_mobile)
    EditText alternateMobile;

    @ViewById(R.id.address1)
    EditText address1;

    @ViewById(R.id.address2)
    EditText address2;

    @ViewById(R.id.zip_code)
    EditText zipCode;

    @ViewById(R.id.state)
    EditText state;

    @ViewById(R.id.city)
    EditText city;

    @ViewById(R.id.proceed)
    Button proceed;

    @Extra
    ShippingAddress shippingAddress;


    List<SimpleObject> states = null;
    List<SimpleObject> cities = null;

    @AfterViews
    void init() {
        toolbar.setNavigationIcon(R.drawable.button_back1);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());
        if (shippingAddress != null) {
            title.setText("Update Address");
            proceed.setText("UPDATE");
            fullName.setText((shippingAddress.getFullName() != null) ? shippingAddress.getFullName() : "");
            mobile.setText((shippingAddress.getPhone() != null) ? shippingAddress.getPhone() : "");
            alternateMobile.setText((shippingAddress.getAlternatePhone() != null) ? shippingAddress.getAlternatePhone() : "");
            address1.setText((shippingAddress.getAddressLine1() != null) ? shippingAddress.getAddressLine1() : "");
            address2.setText((shippingAddress.getAddressLine2() != null) ? shippingAddress.getAddressLine2() : "");
            zipCode.setText((shippingAddress.getZipCode() != null) ? shippingAddress.getZipCode() : "");
            state.setText((shippingAddress.getState() != null) ? shippingAddress.getState() : "");
            city.setText((shippingAddress.getCity() != null) ? shippingAddress.getCity() : "");
            state.setTag(shippingAddress.getStateId() + "");
            city.setTag(shippingAddress.getCityId() + "");

            toolbar.inflateMenu(R.menu.address);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.delete:
                            deleteAddress();
                            return true;
                    }
                    return false;
                }
            });

        } else {
            title.setText("Create Address");
            proceed.setText("CREATE");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Click(R.id.proceed)
    void onProceed() {
        if (isSignUpValid()) {
            if (proceed.getText().toString().equalsIgnoreCase("create")) {
                createAddress();
            } else {
                updateAddress();
            }
        }
    }

    private void createAddress() {
        hideLoader();
        showLoader();
        Call<APIResponse<ShippingAddress>> callback = apiService.shippingAddressCreate(Constants.MB_API_KEY,
                user.getAuthToken(),
                fullName.getText().toString(),
                mobile.getText().toString(),
                alternateMobile.getText().toString(),
                address1.getText().toString(),
                address2.getText().toString(),
                state.getTag().toString(),
                city.getTag().toString(),
                zipCode.getText().toString());
        callback.enqueue(new Callback<APIResponse<ShippingAddress>>() {
            @Override
            public void onResponse(Response<APIResponse<ShippingAddress>> response) {
                hideLoader();
                if (response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(ShippingAddressActivity.this, user);
                                createAddress();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                Util.makeToast(ShippingAddressActivity.this, response.body().getMeta().getMessage());
                            }
                        }
                    } else {
                        Util.makeToast(ShippingAddressActivity.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(ShippingAddressActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                if (t.getMessage() != null) {
                    Log.d("onFailure", t.getMessage());
                    Util.makeToast(ShippingAddressActivity.this, t.getMessage());
                }
            }
        });
    }

    private void updateAddress() {

        hideLoader();
        showLoader();
        Call<APIResponse<ShippingAddress>> callback = apiService.shippingAddressUpdate(Constants.MB_API_KEY,
                user.getAuthToken(),
                shippingAddress.getId(),
                fullName.getText().toString(),
                mobile.getText().toString(),
                alternateMobile.getText().toString(),
                address1.getText().toString(),
                address2.getText().toString(),
                state.getTag().toString(),
                city.getTag().toString(),
                zipCode.getText().toString());
        callback.enqueue(new Callback<APIResponse<ShippingAddress>>() {
            @Override
            public void onResponse(Response<APIResponse<ShippingAddress>> response) {
                hideLoader();
                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().getStatusCode() == 401) {
                        if (response.body().getMeta().isStatus()) {
                            user.setAuthToken(response.body().getMeta().getNewAuthToken());
                            Util.setUser(ShippingAddressActivity.this, user);
                            updateAddress();
                        } else {
                            oopsLogout(response.body().getMeta().getMessage());
                        }
                    } else {
                        if (response.body().getMeta().isStatus()) {
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Util.makeToast(ShippingAddressActivity.this, response.body().getMeta().getMessage());
                        }
                    }
                } else {
                    Util.makeToast(ShippingAddressActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                if (t.getMessage() != null) {
                    Log.d("onFailure", t.getMessage());
                    Util.makeToast(ShippingAddressActivity.this, t.getMessage());
                }
            }
        });
    }

    private void deleteAddress() {
        hideLoader();
        showLoader();
        Call<APIResponse> callback = apiService.shippingAddressDelete(Constants.MB_API_KEY,
                user.getAuthToken(),
                shippingAddress.getId());
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                hideLoader();
                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().getStatusCode() == 401) {
                        if (response.body().getMeta().isStatus()) {
                            user.setAuthToken(response.body().getMeta().getNewAuthToken());
                            Util.setUser(ShippingAddressActivity.this, user);
                            deleteAddress();
                        } else {
                            oopsLogout(response.body().getMeta().getMessage());
                        }
                    } else {
                        if (response.body().getMeta().isStatus()) {
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Util.makeToast(ShippingAddressActivity.this, response.body().getMeta().getMessage());
                        }
                    }
                } else {
                    Util.makeToast(ShippingAddressActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                if (t.getMessage() != null) {
                    Log.d("onFailure", t.getMessage());
                    Util.makeToast(ShippingAddressActivity.this, t.getMessage());
                }
            }
        });
    }

    private boolean isSignUpValid() {
        String validationMessage = "";
        if (Util.textIsEmpty(fullName.getText().toString())) {
            validationMessage = "Please enter name";
            fullName.setError(validationMessage);
        } else if (Util.textIsEmpty(mobile.getText().toString()) || mobile.getText().toString().length() < 10) {
            validationMessage = "Please enter mobile number";
            mobile.setError(validationMessage);
        } else if (Util.textIsEmpty(alternateMobile.getText().toString()) || alternateMobile.getText().toString().equalsIgnoreCase(mobile.getText().toString())) {
            validationMessage = "Please enter alternate mobile number";
            alternateMobile.setError(validationMessage);
        } else if (Util.textIsEmpty(address1.getText().toString())) {
            validationMessage = "Please enter address line 1";
            address1.setError(validationMessage);
        } else if (Util.textIsEmpty(address2.getText().toString())) {
            validationMessage = "Please enter address line 2";
            address2.setError(validationMessage);
        } else if (Util.textIsEmpty(zipCode.getText().toString()) || zipCode.getText().toString().length() < 6) {
            validationMessage = "Please enter zip code";
            zipCode.setError(validationMessage);
        } else if (Util.textIsEmpty(state.getText().toString())) {
            validationMessage = "Please enter state";
            state.setError(validationMessage);
        } else if (Util.textIsEmpty(city.getText().toString())) {
            validationMessage = "Please enter city";
            city.setError(validationMessage);
        }
        return validationMessage.length() == 0;
    }

    @Click(R.id.state)
    void onState() {
        if (states != null) {
            stateDialog();
        } else {
            getStates();
        }
    }

    @Click(R.id.city)
    void onCity() {
        if (state.getTag() != null) {
            if (cities != null) {
                cityDialog();
            } else {
                getCities();
            }
        } else
            Util.makeToast(this, "Please select state first");
    }

    private void getStates() {
        hideLoader();
        showLoader();
        Call<APIResponse<List<SimpleObject>>> callback = apiService.getStates(Constants.MB_API_KEY,
                user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<List<SimpleObject>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<SimpleObject>>> response) {
                hideLoader();
                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().getStatusCode() == 401) {
                        if (response.body().getMeta().isStatus()) {
                            user.setAuthToken(response.body().getMeta().getNewAuthToken());
                            Util.setUser(ShippingAddressActivity.this, user);
                            getStates();
                        } else {
                            oopsLogout(response.body().getMeta().getMessage());
                        }
                    } else {
                        if (response.body().getMeta().isStatus()) {
                            if (response.body().getValues() != null && response.body().getValues().size() > 0) {
                                states = response.body().getValues();
                                stateDialog();
                            }
                        } else {
                            Util.makeToast(ShippingAddressActivity.this, response.body().getMeta().getMessage());
                        }
                    }
                } else {
                    Util.makeToast(ShippingAddressActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                if (t.getMessage() != null) {
                    Log.d("onFailure", t.getMessage());
                    Util.makeToast(ShippingAddressActivity.this, t.getMessage());
                }
            }
        });
    }

    private void getCities() {
        hideLoader();
        showLoader();
        Call<APIResponse<List<SimpleObject>>> callback = apiService.getCities(Constants.MB_API_KEY,
                user.getAuthToken(), state.getTag().toString());
        callback.enqueue(new Callback<APIResponse<List<SimpleObject>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<SimpleObject>>> response) {
                hideLoader();
                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().getStatusCode() == 401) {
                        if (response.body().getMeta().isStatus()) {
                            user.setAuthToken(response.body().getMeta().getNewAuthToken());
                            Util.setUser(ShippingAddressActivity.this, user);
                            getCities();
                        } else {
                            oopsLogout(response.body().getMeta().getMessage());
                        }
                    } else {
                        if (response.body().getMeta().isStatus()) {
                            if (response.body().getValues() != null && response.body().getValues().size() > 0) {
                                cities = response.body().getValues();
                                cityDialog();
                            }else {
                                Util.makeToast(ShippingAddressActivity.this, response.body().getMeta().getMessage());
                            }
                        } else {
                            Util.makeToast(ShippingAddressActivity.this, response.body().getMeta().getMessage());
                        }
                    }
                } else {
                    Util.makeToast(ShippingAddressActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                if (t.getMessage() != null) {
                    Log.d("onFailure", t.getMessage());
                    Util.makeToast(ShippingAddressActivity.this, t.getMessage());
                }
            }
        });
    }

    private void stateDialog() {
        List<String> l1 = new ArrayList<String>();
        for (SimpleObject value : states) {
            l1.add(value.getName());
        }
        Log.d("State Length", l1.size() + " : ");
        CharSequence[] stateArray = l1.toArray(new CharSequence[l1.size()]);

        new AlertDialog.Builder(this)
                .setTitle("Select States")
                .setItems(stateArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        state.setText(states.get(which).getName());
                        state.setTag(states.get(which).getId());
                        city.setTag(null);
                        city.setText("");
                        cities = null;
                    }
                })
                .show();
    }

    private void cityDialog() {
        List<String> l1 = new ArrayList<String>();
        for (SimpleObject value : cities) {
            l1.add(value.getName());
        }
        Log.d("State Length", l1.size() + " : ");
        CharSequence[] stateArray = l1.toArray(new CharSequence[l1.size()]);

        new AlertDialog.Builder(this)
                .setTitle("Select States")
                .setItems(stateArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        city.setText(cities.get(which).getName());
                        city.setTag(cities.get(which).getId());
                    }
                })
                .show();
    }
}
