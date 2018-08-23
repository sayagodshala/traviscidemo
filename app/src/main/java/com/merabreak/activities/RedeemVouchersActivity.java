package com.merabreak.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.merabreak.AppSettings;
import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.adapter.RedeemVoucherListAdapter;
import com.merabreak.models.APIResponse;
import com.merabreak.models.AccountDetails;
import com.merabreak.models.RedeemVoucherModel;
import com.merabreak.network.Connectivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
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
 * Created by ewebcoremac1 on 07/07/18.
 */


@EActivity(R.layout.activity_redeem_voucher)
public class RedeemVouchersActivity extends BaseActivity {

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.pointsNum)
    TextView pointsNum;

    @ViewById(R.id.redeem_voucher_rv)
    RecyclerView raffleContestRv;

    RedeemVoucherListAdapter redeemVoucherListAdapter;

    List<RedeemVoucherModel> redeemVoucherList;
    @ViewById(R.id.problem)
    LinearLayout placeHolder;

    @ViewById(R.id.retry)
    TextView retry;

    @Extra
    String redeemVoucherType;

    @ViewById(R.id.progress)
    LinearLayout progress;


    @AfterViews
    void init() {
        title.setText(R.string.voucher);
        toolbar.setNavigationIcon(R.drawable.mb_back_icon);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());

        redeemVoucherList = new ArrayList<>();



    }


    @Override
    public void onResume() {
        super.onResume();

        if (Connectivity.isConnectedWifi(context)) {
            raffleContestRv.setVisibility(View.VISIBLE);
            placeHolder.setVisibility(View.GONE);
            if(redeemVoucherType != null && !redeemVoucherType.isEmpty())
            {
                if(redeemVoucherType.equals(AppSettings.REDEEM_TYPE_RAFFLE))
                {
                    callRedeemRaffleVouchers();
                }
                else if(redeemVoucherType.equals(AppSettings.REDEEM_TYPE_PRODUCT))
                {
                    callRedeemProductVouchers();
                }
            }
        }else {
            raffleContestRv.setVisibility(View.GONE);
            placeHolder.setVisibility(View.VISIBLE);
            Util.makeToast(context, context.getResources().getString(R.string.no_internet));
        }



        if(redeemVoucherType != null && !redeemVoucherType.isEmpty())
        {
            if(redeemVoucherType.equals(AppSettings.REDEEM_TYPE_RAFFLE))
            {
                title.setText(R.string.raffle_voucher_title);
            }
            else if(redeemVoucherType.equals(AppSettings.REDEEM_TYPE_PRODUCT))
            {
                title.setText(R.string.store_voucher_title);
            }
        }

        getUserAccountDetails();
    }

    @Click(R.id.retry)
    void retryClick()
    {
        if(redeemVoucherType != null && !redeemVoucherType.isEmpty())
        {
            if(redeemVoucherType.equals(AppSettings.REDEEM_TYPE_RAFFLE))
            {
                callRedeemRaffleVouchers();
            }
            else if(redeemVoucherType.equals(AppSettings.REDEEM_TYPE_PRODUCT))
            {
                callRedeemProductVouchers();
            }
        }
    }

    private void inItListener() {

        ((RedeemVoucherListAdapter) redeemVoucherListAdapter).setOnItemClickListener(new RedeemVoucherListAdapter.MyClickListener() {
            @Override
            public void onItemClick(RedeemVoucherModel position, View v) {

                if(redeemVoucherType.equals(AppSettings.REDEEM_TYPE_RAFFLE))
                {
                    RafflesActivity_.intent(context).redeemVoucherModel(position).start();
                }
                else if(redeemVoucherType.equals(AppSettings.REDEEM_TYPE_PRODUCT))
                {
                    StoresActivity_.intent(context).redeemVoucherModel(position).start();
                }
            }
        });
    }

    private void getUserAccountDetails() {
        Call<APIResponse<AccountDetails>> callback = apiService.getAccountDetails(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<AccountDetails>>() {
            @Override
            public void onResponse(Response<APIResponse<AccountDetails>> response) {
                try {
                    if(response.code() == 200 && response.body() != null) {
                        if (response.body().getMeta() != null) {
                            if (response.body().getMeta().getStatusCode() == 401) {
                                if (response.body().getMeta().isStatus()) {
                                    user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                    Util.setUser(RedeemVouchersActivity.this, user);
                                    getUserAccountDetails();
                                } else {
                                    oopsLogout(response.body().getMeta().getMessage());
                                }
                            } else {
                                if (response.body().getMeta().isStatus()) {
                                    if (response.body().getValues() != null) {
                                        Util.saveUserAccountDetails(RedeemVouchersActivity.this, response.body().getValues());
                                        AccountDetails ad = Util.getUserAccountDetails(getApplicationContext());
                                        if (ad != null)
                                            pointsNum.setText(Util.doubleToStringNoDecimal(ad.getCoinBalance()));
                                    } else {
                                        Util.makeToast(RedeemVouchersActivity.this, response.body().getMeta().getMessage());
                                    }
                                } else {
                                    Util.makeToast(RedeemVouchersActivity.this, response.body().getMeta().getMessage());
                                }
                            }
                        } else {
                            Util.makeToast(RedeemVouchersActivity.this, Constants.SOME_THING_WRONG);
                        }
                    } else {
                        Util.makeToast(RedeemVouchersActivity.this, Constants.SOME_THING_WRONG);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }



    private void callRedeemProductVouchers() {

        if (Connectivity.isConnectedWifi(context)) {
            progress.setVisibility(View.VISIBLE);
            raffleContestRv.setVisibility(View.VISIBLE);
            placeHolder.setVisibility(View.GONE);

            Call<APIResponse<List<RedeemVoucherModel>>> callback = apiService.getProductCategoryList(Constants.MB_API_KEY, user.getAuthToken());
            callback.enqueue(new Callback<APIResponse<List<RedeemVoucherModel>>>() {
                @Override
                public void onResponse(Response<APIResponse<List<RedeemVoucherModel>>> response) {
                    progress.setVisibility(View.GONE);
                    //hideLoader(progress);
                    if (response.body() != null && response.code() == 200) {
                        if (response.body().getMeta() != null) {
                            if (response.body().getMeta().getStatusCode() == 401) {
                                if (response.body().getMeta().isStatus()) {
                                    user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                    Util.setUser(RedeemVouchersActivity.this, user);
                                    callRedeemProductVouchers();
                                } else {
                                    oopsLogout(response.body().getMeta().getMessage());
                                }
                            } else {
                                if (response.body().getMeta().isStatus()) {
                                    if (response.body().getValues() != null) {
                                        redeemVoucherList.clear();
                                        redeemVoucherList.addAll(response.body().getValues());

                                        setAdapter();
                                    } else {
                                        Util.makeToast(RedeemVouchersActivity.this, response.body().getMeta().getMessage());
                                    }
                                } else {
                                    Util.makeToast(RedeemVouchersActivity.this, response.body().getMeta().getMessage());
                                }
                            }
                        } else {
                            Util.makeToast(RedeemVouchersActivity.this, Constants.SOME_THING_WRONG);
                        }
                    } else {
                        Util.makeToast(RedeemVouchersActivity.this, Constants.SOME_THING_WRONG);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    progress.setVisibility(View.GONE);
                }
            });
        }else {
            progress.setVisibility(View.GONE);
            raffleContestRv.setVisibility(View.GONE);
            placeHolder.setVisibility(View.VISIBLE);
            Util.makeToast(context, context.getResources().getString(R.string.no_internet));
        }
    }


    private void callRedeemRaffleVouchers() {

        if (Connectivity.isConnectedWifi(context)) {
            progress.setVisibility(View.VISIBLE);
            raffleContestRv.setVisibility(View.VISIBLE);
            placeHolder.setVisibility(View.GONE);

            Call<APIResponse<List<RedeemVoucherModel>>> callback = apiService.getRaffleCategoryList(Constants.MB_API_KEY, user.getAuthToken());
            callback.enqueue(new Callback<APIResponse<List<RedeemVoucherModel>>>() {
                @Override
                public void onResponse(Response<APIResponse<List<RedeemVoucherModel>>> response) {
                    progress.setVisibility(View.GONE);
                    //hideLoader(progress);
                    if (response.body() != null && response.code() == 200) {
                        if (response.body().getMeta() != null) {
                            if (response.body().getMeta().getStatusCode() == 401) {
                                if (response.body().getMeta().isStatus()) {
                                    user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                    Util.setUser(RedeemVouchersActivity.this, user);
                                    callRedeemRaffleVouchers();
                                } else {
                                    oopsLogout(response.body().getMeta().getMessage());
                                }
                            } else {
                                if (response.body().getMeta().isStatus()) {
                                    if (response.body().getValues() != null) {
                                        redeemVoucherList.clear();
                                        redeemVoucherList.addAll(response.body().getValues());

                                        setAdapter();
                                    } else {
                                        Util.makeToast(RedeemVouchersActivity.this, response.body().getMeta().getMessage());
                                    }
                                } else {
                                    Util.makeToast(RedeemVouchersActivity.this, response.body().getMeta().getMessage());
                                }
                            }
                        } else {
                            Util.makeToast(RedeemVouchersActivity.this, Constants.SOME_THING_WRONG);
                        }
                    } else {
                        Util.makeToast(RedeemVouchersActivity.this, Constants.SOME_THING_WRONG);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    progress.setVisibility(View.GONE);
                }
            });
        }else {
            progress.setVisibility(View.GONE);
            raffleContestRv.setVisibility(View.GONE);
            placeHolder.setVisibility(View.VISIBLE);
            Util.makeToast(context, context.getResources().getString(R.string.no_internet));
        }
    }


    private void setAdapter() {

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        raffleContestRv.setLayoutManager(manager);
        raffleContestRv.setNestedScrollingEnabled(false);
        raffleContestRv.setHasFixedSize(false);

        redeemVoucherListAdapter = new RedeemVoucherListAdapter(redeemVoucherList, this);
        raffleContestRv.setAdapter(redeemVoucherListAdapter);

        inItListener();
    }
}
