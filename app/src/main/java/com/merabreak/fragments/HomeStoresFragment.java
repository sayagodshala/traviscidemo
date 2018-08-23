package com.merabreak.fragments;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.merabreak.BaseFragment;
import com.merabreak.Constants;
import com.merabreak.CustomListAdapter;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.activities.StoreActivity_;
import com.merabreak.adapter.StoreRecyclerViewAdapter;
import com.merabreak.itemViews.StoreItemView;
import com.merabreak.itemViews.StoreItemView_;
import com.merabreak.models.APIResponse;
import com.merabreak.models.AccountDetails;
import com.merabreak.models.Store;
import com.merabreak.network.Connectivity;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

@EFragment(R.layout.home_stores)
public class HomeStoresFragment extends BaseFragment {

    @ViewById(R.id.items)
    RecyclerView recyclerListItems;

//    @ViewById(R.id.items)
//    ListView itemsList;

    @ViewById(R.id.problem)
    LinearLayout problem;

    @ViewById(R.id.error)
    TextView error;

    @ViewById(R.id.retry)
    Button retry;

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter recyclerListItemsAdapter;

    public List<Store> items = new ArrayList<Store>();

    private CustomListAdapter itemsAdapter = null;

    @ViewById(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    private boolean initiated = false;

    @AfterViews
    void init() {
//        Log.d("Initiate A ", initiated + "");
////        if (!initiated) {
//            makeViewsGone();
//            LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
//            recyclerListItems.setLayoutManager(manager);
//            recyclerListItems.setNestedScrollingEnabled(false);
//            recyclerListItems.setHasFixedSize(false);
////        }


    }

    @Override
    public void onResume() {
        super.onResume();
        makeViewsGone();
        if (!Connectivity.isConnected(getActivity())) {
            setProblem("No Internet Found, Seems like you are offline at the moment", "1");
        } else {
            items = Util.getHomeStores(getActivity());
            if (items.size() > 0) {
                drawItems();
                swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipeRefresh.setRefreshing(true);
                        getItems();
                    }
                });
            } else {
                getItems();
            }
        }

    }

    private void getItems() {
        if (!swipeRefresh.isRefreshing())
            showLoader();
        Call<APIResponse<List<Store>>> callback = apiService.getStores(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<List<Store>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Store>>> response) {

                problem.setVisibility(View.GONE);
                if (swipeRefresh.isRefreshing()) {
                    swipeRefresh.setRefreshing(false);
                } else {
                    hideLoader();
                }
                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().isStatus()) {

                        if (response.body().getValues() != null && response.body().getValues().size() > 0) {
                            items = response.body().getValues();
                            Util.saveHomeStores(getActivity(), items);
                            drawItems();
                        } else {
                            setProblem(response.body().getMeta().getMessage(), "3");
                        }
                    } else {
                        setProblem(response.body().getMeta().getMessage(), "3");
                    }
                } else {
                    setProblem(Constants.SOME_THING_WRONG, "2");
                }
            }


            @Override
            public void onFailure(Throwable t) {
                if (swipeRefresh.isRefreshing()) {
                    swipeRefresh.setRefreshing(false);
                } else {
                    hideLoader();
                }
                setProblem(Constants.SOME_THING_WRONG, "2");
            }
        });
    }

    private void drawItems() {
        if (getActivity() != null) {
            recyclerListItemsAdapter = new StoreRecyclerViewAdapter(items, getActivity());
            recyclerListItems.setAdapter(recyclerListItemsAdapter);
            recyclerListItems.setVisibility(View.VISIBLE);

            LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
            recyclerListItems.setLayoutManager(manager);
            recyclerListItems.setNestedScrollingEnabled(false);
            recyclerListItems.setHasFixedSize(false);

            ((StoreRecyclerViewAdapter) recyclerListItemsAdapter).setOnItemClickListener(new StoreRecyclerViewAdapter.MyClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    //Log.d("Store Detail", new Gson().toJson(items.get(position)));

                    if (!Util.isUserLoggedIn(getActivity())) {
                        Util.makeToast(getActivity(), "Please signup to play this challenge");
                        return;
                    }


                    AccountDetails ad = Util.getUserAccountDetails(getActivity());
                    if (ad != null) {
                        if (ad.getCoinBalance() > Integer.parseInt(items.get(position).getCoins())) {
                            StoreActivity_.intent(getActivity()).id(items.get(position).getSlug()).start();
                        } else {
                            Util.makeToast(getActivity(), "You dont have enough coins to buy this product");
                        }
                    }
                    //Log.i("Item Click", " Clicked on Item " + position);
                }
            });

            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefresh.setRefreshing(true);
                    getItems();
                }
            });

            if (items.size() > 0)
                problem.setVisibility(View.GONE);

        }
    }

    private StoreItemView storeItemView() {
        StoreItemView item = StoreItemView_.build(getActivity());
        return item;
    }


    private void setProblem(String errorString, String tag) {
        problem.setVisibility(View.VISIBLE);
        error.setText(errorString);
        error.setTag(tag);
        if (tag.equalsIgnoreCase("1")) {
            retry.setText("SETTINGS");
        } else {
            retry.setText("RETRY");
        }
    }

    @Click(R.id.retry)
    void onRetryClick() {

        if (error.getTag() != null) {
            if (error.getTag().toString().equalsIgnoreCase("1")) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
            } else if (error.getTag().toString().equalsIgnoreCase("2")) {
                makeViewsGone();
                getItems();
            } else if (error.getTag().toString().equalsIgnoreCase("3")) {
                makeViewsGone();
                getItems();
            }
        }
    }

    private void makeViewsGone() {
        //Log.d("Initiate MV", "1");
        problem.setVisibility(View.GONE);
        recyclerListItems.setVisibility(View.GONE);
    }
}