package com.merabreak.activities;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.EndlessRecyclerViewScrollListener;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.adapter.StoreRecyclerViewAdapter;
import com.merabreak.models.APIResponse;
import com.merabreak.models.AccountDetails;
import com.merabreak.models.RedeemVoucherModel;
import com.merabreak.models.Store;
import com.google.gson.Gson;

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
@EActivity(R.layout.stores)
public class StoresActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.pointsNum)
    TextView pointsNum;

    @ViewById(R.id.error)
    TextView error;

    @ViewById(R.id.empty)
    LinearLayout empty;

    @ViewById(R.id.progress)
    LinearLayout progress;

    @ViewById(R.id.items)
    RecyclerView recyclerListItems;

    @Extra
    RedeemVoucherModel redeemVoucherModel;

    @Extra
    String slug = "";

    private StoreRecyclerViewAdapter recyclerListItemsAdapter;

    public List<Store> items = new ArrayList<Store>();
    int pages = 1, limit = 15;

    @AfterViews
    void init() {
        empty.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.mb_back_icon);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());
        title.setText(R.string.store_voucher_title);
      //  LinearLayoutManager manager = new LinearLayoutManager(this);
      //  manager.setOrientation(LinearLayoutManager.VERTICAL);

        if(redeemVoucherModel !=null && !Util.textIsEmpty(redeemVoucherModel.getSlug()))
            slug = redeemVoucherModel.getSlug();
        else if(Util.textIsEmpty(slug))
            slug = "";


        GridLayoutManager manager1 = new GridLayoutManager(this, 2);
        recyclerListItems.setLayoutManager(manager1);
        recyclerListItems.setNestedScrollingEnabled(false);
        recyclerListItems.setHasFixedSize(false);
        //getStores();
        recyclerListItems.addOnScrollListener(new EndlessRecyclerViewScrollListener(manager1) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if(redeemVoucherModel !=null)
                    getStores(slug);
            }
        });
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



        getStores(slug);
        getUserAccountDetails();
    }

    private void getStores(String slug) {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse<List<Store>>> callback = apiService.getStorePaginationData(Constants.MB_API_KEY, user.getAuthToken(), slug, pages, limit);
        callback.enqueue(new Callback<APIResponse<List<Store>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Store>>> response) {
                hideLoader(progress);
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(StoresActivity.this, user);
                                getStores(slug);
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                if (response.body().getValues() != null && response.body().getValues().size() > 0) {
                                    if (pages == 1) {
                                        items = response.body().getValues();
                                        drawItems();
                                        //Log.d("responce", new Gson().toJson(response.body().getValues()));
                                    } else {
                                        List<Store> items1 = response.body().getValues();
                                        //Log.d("responce1", new Gson().toJson(response.body().getValues()));
                                        for (int i = 0; i < items1.size(); i++) {
                                            Store store = items1.get(i);
                                            recyclerListItemsAdapter.mDataset.add(store);
                                        }
                                        recyclerListItemsAdapter.notifyDataSetChanged();
                                    }
                                    pages++;

                                } else {
                                    if (pages == 1) {
                                        empty.setVisibility(View.VISIBLE);
                                        error.setText(R.string.store_no_products);
                                    }
                                }
                            } else {
                                if (pages == 1) {
                                    empty.setVisibility(View.VISIBLE);
                                    error.setText(response.body().getMeta().getMessage());
                                }
                            }
                        }

                    } else {
                        if (pages == 1) {
                            empty.setVisibility(View.VISIBLE);
                            error.setText(Constants.SOME_THING_WRONG);
                        }
                    }
                } else {
                    if (pages == 1) {
                        empty.setVisibility(View.VISIBLE);
                        error.setText(Constants.SOME_THING_WRONG);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                empty.setVisibility(View.VISIBLE);
                error.setText(Constants.SOME_THING_WRONG);
                if (t.getMessage() != null)
                    Log.d("JSONError", t.getMessage());
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
                                    Util.setUser(StoresActivity.this, user);
                                    getUserAccountDetails();
                                } else {
                                    oopsLogout(response.body().getMeta().getMessage());
                                }
                            } else {
                                if (response.body().getMeta().isStatus()) {
                                    if (response.body().getValues() != null) {
                                        Util.saveUserAccountDetails(StoresActivity.this, response.body().getValues());
                                        AccountDetails ad = Util.getUserAccountDetails(getApplicationContext());
                                        if (ad != null)
                                            pointsNum.setText(doubleToStringNoDecimal(ad.getCoinBalance()));
                                    } else {
                                        Util.makeToast(StoresActivity.this, response.body().getMeta().getMessage());
                                    }
                                } else {
                                    Util.makeToast(StoresActivity.this, response.body().getMeta().getMessage());
                                }
                            }
                        } else {
                            Util.makeToast(StoresActivity.this, Constants.SOME_THING_WRONG);
                        }
                    } else {
                        Util.makeToast(StoresActivity.this, Constants.SOME_THING_WRONG);
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

/*
    private void getStorePaginationData() {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse<List<Store>>> callback = apiService.getStorePaginationData(Constants.MB_API_KEY, user.getAuthToken(), pages, limit);
        callback.enqueue(new Callback<APIResponse<List<Store>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Store>>> response) {
                hideLoader(progress);
                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().isStatus()) {
                        if (response.body().getValues() != null && response.body().getValues().size() > 0) {
                            items = response.body().getValues();
                            drawItems();
                        } else {
                            empty.setVisibility(View.VISIBLE);
                            error.setText(response.body().getMeta().getMessage());
                        }
                    } else {
                        empty.setVisibility(View.VISIBLE);
                        error.setText(response.body().getMeta().getMessage());
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
                if (t.getMessage() != null)
                    Log.d("JSONError", t.getMessage());
            }
        });
    }
*/

    private void drawItems() {
        recyclerListItemsAdapter = new StoreRecyclerViewAdapter(items, this);
        recyclerListItems.setAdapter(recyclerListItemsAdapter);
        ((StoreRecyclerViewAdapter) recyclerListItemsAdapter).setOnItemClickListener(new StoreRecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
               // Log.i("Item Click", " Clicked on Item " + position);
                //      if (Util.isUserLoggedIn(StoresActivity.this))
                StoreActivity_.intent(StoresActivity.this).id(items.get(position).getSlug()).start();
                //    else
                //      Util.makeToast(StoresActivity.this, "Please signup to buy this product");
            }
        });
    }
}
