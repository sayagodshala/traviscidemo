package com.merabreak.activities;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.adapter.RaffleRecyclerViewAdapter;
import com.merabreak.models.APIResponse;
import com.merabreak.models.AccountDetails;
import com.merabreak.models.Raffle;
import com.merabreak.models.RedeemVoucherModel;

import org.androidannotations.annotations.AfterViews;
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
 * Created by Ewebcore on 05-Feb-16.
 */
@EActivity(R.layout.raffles)
public class RafflesActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.progress)
    LinearLayout progress;

    @ViewById(R.id.pointsNum)
    TextView pointsNum;

    @ViewById(R.id.error)
    TextView error;

    @ViewById(R.id.empty)
    LinearLayout empty;

    @ViewById(R.id.items)
    RecyclerView recyclerListItems;

    @Extra
    String slug = "";

    @Extra
    RedeemVoucherModel redeemVoucherModel;

    private RecyclerView.Adapter recyclerListItemsAdapter;
    public List<Raffle> items = new ArrayList<Raffle>();

    @AfterViews
    void init() {
        empty.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.mb_back_icon);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());
        title.setText(R.string.raffle_voucher_title);
      //  LinearLayoutManager manager = new LinearLayoutManager(this);
     //   manager.setOrientation(LinearLayoutManager.VERTICAL);
        GridLayoutManager manager1 = new GridLayoutManager(this, 2);
        recyclerListItems.setLayoutManager(manager1);
        recyclerListItems.setNestedScrollingEnabled(false);
        recyclerListItems.setHasFixedSize(false);

        if(redeemVoucherModel !=null && !Util.textIsEmpty(redeemVoucherModel.getSlug()))
            slug = redeemVoucherModel.getSlug();
        else if(Util.textIsEmpty(slug))
            slug = "";

    }

    public static String doubleToStringNoDecimal(double d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter .applyPattern("#,###");
        return formatter.format(d);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(redeemVoucherModel !=null && !Util.textIsEmpty(redeemVoucherModel.getSlug()))
            slug = redeemVoucherModel.getSlug();
        else if(Util.textIsEmpty(slug))
            slug = "";

        getRaffles(slug);

        getUserAccountDetails();
    }

    private void getRaffles(String slug) {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse<List<Raffle>>> callback = apiService.getRaffles(Constants.MB_API_KEY, user.getAuthToken(), slug);
        callback.enqueue(new Callback<APIResponse<List<Raffle>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Raffle>>> response) {
                hideLoader(progress);
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().isStatus()) {
                            if (response.body().getValues() != null && response.body().getValues().size() > 0) {
                                items = response.body().getValues();
                                drawItems();
                            } else {
                                empty.setVisibility(View.VISIBLE);
                                error.setText(R.string.raffle_empty);
                            }
                        } else {
                            empty.setVisibility(View.VISIBLE);
                            error.setText(response.body().getMeta().getMessage());
                        }
                    } else {
                        empty.setVisibility(View.VISIBLE);
                        error.setText(Constants.SOME_THING_WRONG);
                    }
                } else {
                    empty.setVisibility(View.VISIBLE);
                    error.setText(Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                empty.setVisibility(View.VISIBLE);
                error.setText(Constants.SOME_THING_WRONG);
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
                                    Util.setUser(RafflesActivity.this, user);
                                    getUserAccountDetails();
                                } else {
                                    oopsLogout(response.body().getMeta().getMessage());
                                }
                            } else {
                                if (response.body().getMeta().isStatus()) {
                                    if (response.body().getValues() != null) {
                                        Util.saveUserAccountDetails(RafflesActivity.this, response.body().getValues());
                                        AccountDetails ad = Util.getUserAccountDetails(getApplicationContext());
                                        if (ad != null)
                                            pointsNum.setText(doubleToStringNoDecimal(ad.getCoinBalance()));
                                    } else {
                                        Util.makeToast(RafflesActivity.this, response.body().getMeta().getMessage());
                                    }
                                } else {
                                    Util.makeToast(RafflesActivity.this, response.body().getMeta().getMessage());
                                }
                            }
                        } else {
                            Util.makeToast(RafflesActivity.this, Constants.SOME_THING_WRONG);
                        }
                    } else {
                        Util.makeToast(RafflesActivity.this, Constants.SOME_THING_WRONG);
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

    private void drawItems() {
        recyclerListItemsAdapter = new RaffleRecyclerViewAdapter(items, this);
        recyclerListItems.setAdapter(recyclerListItemsAdapter);
        ((RaffleRecyclerViewAdapter) recyclerListItemsAdapter).setOnItemClickListener(new RaffleRecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                RaffleActivity_.intent(RafflesActivity.this).id(items.get(position).getSlug()).start();
            }

            @Override
            public void setSelected(int position, View v) {
                ((RaffleRecyclerViewAdapter) recyclerListItemsAdapter).setSelected(position);
            }
        });
    }
}
