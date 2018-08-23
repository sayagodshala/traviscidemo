package com.merabreak.fragments;

import android.content.Intent;
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
import com.merabreak.adapter.CoinRecyclerViewAdapter;
import com.merabreak.models.APIResponse;
import com.merabreak.models.Coin;
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


@EFragment(R.layout.coins_history)
public class UserCoinsFragment extends BaseFragment {

    @ViewById(R.id.items)
    RecyclerView recyclerListItems;

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter recyclerListItemsAdapter;

    public List<Coin> items = new ArrayList<Coin>();

    @ViewById(R.id.problem)
    LinearLayout problem;

    @ViewById(R.id.progress)
    LinearLayout progress;

    @ViewById(R.id.error)
    TextView error;

    @ViewById(R.id.retry)
    Button retry;

    @AfterViews
    void init() {
        makeViewsGone();
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        manager.setReverseLayout(false);
        manager.setStackFromEnd(false);
        recyclerListItems.setLayoutManager(manager);
        recyclerListItems.setNestedScrollingEnabled(false);
        recyclerListItems.setHasFixedSize(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!Connectivity.isConnected(getActivity())) {
            makeViewsGone();
            setProblem(getResources().getString(R.string.no_internet), "1");
        } else {
            makeViewsGone();
            //Log.d("Challenges Fetch", "initiated");
            getHistory();
        }
    }

    private void getHistory() {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse<List<Coin>>> callback = apiService.getUserCoinsHistory(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<List<Coin>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Coin>>> response) {
                problem.setVisibility(View.GONE);
                hideLoader(progress);
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(getActivity(), user);
                                getHistory();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                if (response.body().getValues() != null && response.body().getValues().size() > 0) {
                                    items = response.body().getValues();
                                    //Log.d("coins his resp", new Gson().toJson(items));
                                    drawItems();
                                } else {
                                    setProblem(getResources().getString(R.string.user_points_frag_prob_three), "3");
                                }
                            } else {
                                setProblem(response.body().getMeta().getMessage(), "3");
                            }
                        }
                    } else {
                        setProblem(Constants.SOME_THING_WRONG, "2");
                    }
                } else {
                    setProblem(Constants.SOME_THING_WRONG, "2");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                setProblem(Constants.SOME_THING_WRONG, "2");
            }
        });
    }

    private void drawItems() {
        if (getActivity() != null) {
            problem.setVisibility(View.GONE);
            recyclerListItemsAdapter = new CoinRecyclerViewAdapter(items, getActivity());
            recyclerListItems.setAdapter(recyclerListItemsAdapter);
            recyclerListItems.setVisibility(View.VISIBLE);
            ((CoinRecyclerViewAdapter) recyclerListItemsAdapter).setOnItemClickListener(new CoinRecyclerViewAdapter.MyClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    //Log.i("Item Click", " Clicked on Item " + position);

                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void setProblem(String errorString, String tag) {
        problem.setVisibility(View.VISIBLE);
        error.setText(errorString);
        error.setTag(tag);
        if (tag.equalsIgnoreCase("1")) {
            retry.setText(R.string.settings_text);
        } else {
            retry.setText(R.string.retry_text);
        }
    }

    @Click(R.id.retry)
    void onRetryClick() {
        if (error.getTag() != null) {
            if (error.getTag().toString().equalsIgnoreCase("1")) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
            } else if (error.getTag().toString().equalsIgnoreCase("2")) {
                makeViewsGone();
                getHistory();
            } else if (error.getTag().toString().equalsIgnoreCase("3")) {
                makeViewsGone();
                getHistory();
            }
        }
    }

    private void makeViewsGone() {
        problem.setVisibility(View.GONE);
        recyclerListItems.setVisibility(View.GONE);
    }
}