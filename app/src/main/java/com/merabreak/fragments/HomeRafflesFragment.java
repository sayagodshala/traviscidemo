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
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.adapter.RaffleRecyclerViewAdapter;
import com.merabreak.models.APIResponse;
import com.merabreak.models.Raffle;
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

@EFragment(R.layout.home_raffles)
public class HomeRafflesFragment extends BaseFragment {

    @ViewById(R.id.items)
    RecyclerView recyclerListItems;

    @ViewById(R.id.problem)
    LinearLayout problem;

    @ViewById(R.id.error)
    TextView error;

    @ViewById(R.id.retry)
    Button retry;

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter recyclerListItemsAdapter;

    public List<Raffle> items = new ArrayList<Raffle>();
    private int selectedRafflePos = -1;

    @ViewById(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    private boolean initiated = false;

    @AfterViews
    void init() {


    }

    @Override
    public void onResume() {
        super.onResume();

        makeViewsGone();
        if (!Connectivity.isConnected(getActivity())) {
            setProblem("No Internet Found, Seems like you are offline at the moment", "1");
        } else {
            items = Util.getHomeRaffles(getActivity());
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

        Call<APIResponse<List<Raffle>>> callback = apiService.getRaffles();
        callback.enqueue(new Callback<APIResponse<List<Raffle>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Raffle>>> response) {
                problem.setVisibility(View.GONE);
                if (swipeRefresh.isRefreshing()) {
                    swipeRefresh.setRefreshing(false);
                } else {
                    hideLoader();
                }

                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().isStatus()) {
                        if (response.body().getValues() != null && response.body().getValues().size() > 0) {
                            Util.saveHomeRaffles(getActivity(), response.body().getValues());
                            items = response.body().getValues();
                           // Log.d("Raffles response>>>>", new Gson().toJson(response.body().getValues()));
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
            recyclerListItems.setVisibility(View.VISIBLE);
            recyclerListItemsAdapter = new RaffleRecyclerViewAdapter(items, getActivity());
            recyclerListItems.setAdapter(recyclerListItemsAdapter);

            LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
            recyclerListItems.setLayoutManager(manager);
            recyclerListItems.setNestedScrollingEnabled(false);
            recyclerListItems.setHasFixedSize(false);

            ((RaffleRecyclerViewAdapter) recyclerListItemsAdapter).setOnItemClickListener(new RaffleRecyclerViewAdapter.MyClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                   // Log.i("Item Click", " Clicked on Item " + position);

                    if (Util.isUserLoggedIn(getActivity()))
                        buyRaffle(items.get(position));
                    else
                        Util.makeToast(getActivity(), "Please signup to buy this raffle");

                }

                @Override
                public void setSelected(int position, View v) {
                    ((RaffleRecyclerViewAdapter) recyclerListItemsAdapter).setSelected(position);
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


    private void buyRaffle(Raffle raffle) {


        hideLoader();
        showLoader();
        Call<APIResponse> callback = apiService.purchaseRaffle(Constants.MB_API_KEY, user.getAuthToken(), raffle.getSlug(), "");
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                hideLoader();
                if (response.body().getMeta() != null) {
                    Util.makeToast(getActivity(), response.body().getMeta().getMessage());
                    if (response.body().getMeta().isStatus()) {

                    }
                } else {
                    Util.makeToast(getActivity(), Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                if (t.getMessage() != null) {
                    Log.d("JSONError", t.getMessage());
                    Util.makeToast(getActivity(), t.getMessage());
                } else {
                    Log.d("API Error", Constants.SOME_THING_WRONG);
                }
            }
        });
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
        problem.setVisibility(View.GONE);
        recyclerListItems.setVisibility(View.GONE);
    }

}