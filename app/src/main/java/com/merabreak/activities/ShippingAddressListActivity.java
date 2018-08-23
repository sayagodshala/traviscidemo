package com.merabreak.activities;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.MainActivity_;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.adapter.ShippingAddressRecyclerViewAdapter;
import com.merabreak.controls.DividerItemDecoration;
import com.merabreak.models.APIResponse;
import com.merabreak.models.AccountDetails;
import com.merabreak.models.ShippingAddress;
import com.merabreak.models.Store;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by Ewebcore on 05-Feb-16.
 */
@EActivity(R.layout.addresses)
public class ShippingAddressListActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.error)
    TextView error;

    @ViewById(R.id.empty)
    LinearLayout empty;

    @ViewById(R.id.items)
    RecyclerView recyclerListItems;

    @Extra
    Store store;

    @Extra
    String why;

    @ViewById(R.id.proceed)
    Button proceed;

    private RecyclerView.Adapter recyclerListItemsAdapter;
    List<ShippingAddress> items = new ArrayList<ShippingAddress>();
    private String addressId;

    @AfterViews
    void init() {
        empty.setVisibility(View.GONE);

        if (store != null) {
            title.setText("Select Shipping Addresses");
        } else {
            title.setText("Shipping Addresses");
            proceed.setVisibility(View.GONE);
        }

        toolbar.setNavigationIcon(R.drawable.button_back1);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());
        toolbar.inflateMenu(R.menu.address_list);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add:
                        ShippingAddressActivity_.intent(ShippingAddressListActivity.this).startForResult(Constants.ADDRESS_UPDATE);
                        return true;
                }
                return false;
            }
        });
        getAddresses(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getAddresses(boolean preloader) {
        if (preloader)
            showLoader();
        Call<APIResponse<List<ShippingAddress>>> callback = apiService.getShippingAddressList(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<List<ShippingAddress>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<ShippingAddress>>> response) {
                if (preloader)
                    hideLoader();
                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().getStatusCode() == 401) {
                        if (response.body().getMeta().isStatus()) {
                            user.setAuthToken(response.body().getMeta().getNewAuthToken());
                            Util.setUser(ShippingAddressListActivity.this, user);
                            getAddresses(preloader);
                        } else {
                            oopsLogout(response.body().getMeta().getMessage());
                        }
                    } else {
                        if (response.body().getMeta().isStatus()) {
                            if (response.body().getValues() != null && response.body().getValues().size() > 0) {
                                items = response.body().getValues();
                                drawAddresses();
                            } else {
                                empty.setVisibility(View.VISIBLE);
                                error.setText(response.body().getMeta().getMessage());
                            }
                        } else {
                            empty.setVisibility(View.VISIBLE);
                            error.setText(response.body().getMeta().getMessage());
                        }
                    }
                } else {
                    empty.setVisibility(View.VISIBLE);
                    error.setText(Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                empty.setVisibility(View.VISIBLE);
                error.setText(Constants.SOME_THING_WRONG);
            }
        });
    }

    private void drawAddresses() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerListItems.setLayoutManager(manager);
        recyclerListItems.setNestedScrollingEnabled(false);
        recyclerListItems.setHasFixedSize(false);

        recyclerListItemsAdapter = new ShippingAddressRecyclerViewAdapter(items, this, (store != null) ? "selection" : "");
        recyclerListItems.setAdapter(recyclerListItemsAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);

        ((ShippingAddressRecyclerViewAdapter) recyclerListItemsAdapter).setOnItemClickListener(new ShippingAddressRecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i("Item Click", " Clicked on Item " + position);
                ShippingAddressActivity_.intent(ShippingAddressListActivity.this).shippingAddress(items.get(position)).startForResult(Constants.ADDRESS_UPDATE);
            }

            @Override
            public void onEditClick(int position, View v) {
                ShippingAddressActivity_.intent(ShippingAddressListActivity.this).shippingAddress(items.get(position)).startForResult(Constants.ADDRESS_UPDATE);
            }

            @Override
            public void onCheckClick(int position, View v) {
                ((ShippingAddressRecyclerViewAdapter) recyclerListItemsAdapter).selectedItem(position);
                addressId = items.get(position).getId();
                proceed.setText("Proceed");
            }
        });
    }

    @Click(R.id.proceed)
    void onProceed() {
        if (items.size() > 0) {
            if (addressId != null)
                buyStore();
            else
                Util.makeToast(this, "Please select shipping address");
        } else {
            Util.makeToast(this, "Please create and select shipping address");
        }
    }

    private void buyStore() {
        hideLoader();
        showLoader();
        Call<APIResponse> callback = apiService.purchaseProduct(Constants.MB_API_KEY, user.getAuthToken(), store.getId(), addressId);
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                hideLoader();
                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().getStatusCode() == 401) {
                        if (response.body().getMeta().isStatus()) {
                            user.setAuthToken(response.body().getMeta().getNewAuthToken());
                            Util.setUser(ShippingAddressListActivity.this, user);
                            buyStore();
                        } else {
                            oopsLogout(response.body().getMeta().getMessage());
                        }
                    } else {
                        Util.makeToast(ShippingAddressListActivity.this, response.body().getMeta().getMessage());
                        if (response.body().getMeta().isStatus()) {
                            addressId = null;
                            AccountDetails ad = Util.getUserAccountDetails(ShippingAddressListActivity.this);
                            ad.setTotalProducts(ad.getTotalProducts() + 1);
                            Util.saveUserAccountDetails(ShippingAddressListActivity.this, ad);
                            MainActivity_.intent(ShippingAddressListActivity.this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
                        }else {
                            Util.makeToast(ShippingAddressListActivity.this, response.body().getMeta().getMessage());
                        }
                    }
                } else {
                    Util.makeToast(ShippingAddressListActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                if (t.getMessage() != null)
                    Log.d("JSONError", t.getMessage());

                Util.makeToast(ShippingAddressListActivity.this, Constants.SOME_THING_WRONG);
            }
        });
    }

    @OnActivityResult(Constants.ADDRESS_UPDATE)
    void catchOnActivityResult(int resultCode) {
        if (RESULT_OK == resultCode) {
            getAddresses(false);
        }
    }
}
