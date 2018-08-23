package com.merabreak.activities;

import android.graphics.Typeface;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.merabreak.AppSettings;
import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.MainActivity;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.models.APIResponse;
import com.merabreak.models.StripParams;
import com.merabreak.models.TotalRaffleStore;
import com.merabreak.network.Connectivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by Vinay on 10/6/2016.
 */
@EActivity(R.layout.redeem_points)
public class RedeemPointsActivity extends BaseActivity {

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.recharge_text)
    TextView recharge_text;

    @ViewById(R.id.raffle_text)
    TextView raffle_text;

    @ViewById(R.id.store_text)
    TextView store_text;

    @ViewById(R.id.total_raffle)
    TextView total_raffle;

    @ViewById(R.id.total_store)
    TextView total_store;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.redeem_recharge)
    LinearLayout redeemRecharge;

    @ViewById(R.id.redeem_raffle)
    LinearLayout redeemRaffle;

    @ViewById(R.id.redeem_store)
    LinearLayout redeemStore;

    String raffle, store;
    int rechargeEnabled;

    @AfterViews
    void init() {
        title.setText(R.string.redeem_points_title);
        toolbar.setNavigationIcon(R.drawable.mb_back_icon);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());
    }

    private void getTotalRaffleProduct() {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/roboto_medium.ttf");
        recharge_text.setTypeface(font);
        raffle_text.setTypeface(font);
        store_text.setTypeface(font);
        Call<APIResponse<TotalRaffleStore>> callback = apiService.getTotalRaffleStore(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<TotalRaffleStore>>() {
            @Override
            public void onResponse(Response<APIResponse<TotalRaffleStore>> response) {
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(RedeemPointsActivity.this, user);
                                getTotalRaffleProduct();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                if (response.body().getValues().getTotal_raffle() != null && response.body().getValues().getTotal_store() != null) {
                                    raffle = response.body().getValues().getTotal_raffle();
                                    store = response.body().getValues().getTotal_store();
                                    rechargeEnabled = response.body().getValues().getRechargeEnabled();
                                } else {
                                    Util.makeToast(RedeemPointsActivity.this, response.body().getMeta().getMessage());
                                }
                                setData();
                            } else {
                                Util.makeToast(RedeemPointsActivity.this, response.body().getMeta().getMessage());
                            }
                        }
                    } else {
                        Util.makeToast(RedeemPointsActivity.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(RedeemPointsActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void setData(){
        total_raffle.setText(raffle + " " + RedeemPointsActivity.this.getResources().getString(R.string.redeem_items));
        total_store.setText(store + " " + RedeemPointsActivity.this.getResources().getString(R.string.redeem_items));
        if(rechargeEnabled == 1)
            redeemRecharge.setVisibility(View.VISIBLE);
        else
            redeemRecharge.setVisibility(View.GONE);
    }

    @Click(R.id.redeem_recharge)
    void makeRecharge() {
        if (Connectivity.isConnected(context)) {
            if (Util.isUserLoggedIn(context)) {
                RechargeActivity_.intent(context).start();
            } else {
                Util.makeToast(context, getResources().getString(R.string.redeem_not_signin));
            }
        } else {
            Util.makeToast(context, getResources().getString(R.string.no_internet));
        }
    }

    @Click(R.id.redeem_raffle)
    void buyRaffle() {
        if (Connectivity.isConnected(context))
        {
            //RafflesActivity_.intent(context).start();

            RedeemVouchersActivity_.intent(context).redeemVoucherType(AppSettings.REDEEM_TYPE_RAFFLE).start();
        }

        else
            Util.makeToast(context, getResources().getString(R.string.no_internet));

    }

    @Click(R.id.redeem_store)
    void buyStore() {
        if (Connectivity.isConnected(context)) {
            RedeemVouchersActivity_.intent(context).redeemVoucherType(AppSettings.REDEEM_TYPE_PRODUCT).start();
            //StoresActivity_.intent(context).start();
        }
        else
            Util.makeToast(context, getResources().getString(R.string.no_internet));
    }

    @Override
    public void onResume() {
        super.onResume();
        getTotalRaffleProduct();
    }
}
