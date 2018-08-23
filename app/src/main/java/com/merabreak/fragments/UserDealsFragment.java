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
import com.merabreak.adapter.DealsHistoryRecyclerViewAdapter;
import com.merabreak.models.APIResponse;
import com.merabreak.models.challenge.Challenge;
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

/**
 * Created by Vinay on 12/2/2016.
 */
@EFragment(R.layout.user_deals)
public class UserDealsFragment extends BaseFragment {

    @ViewById(R.id.items)
    RecyclerView recyclerListItems;

    private DealsHistoryRecyclerViewAdapter dealsHistoryRecyclerViewAdapter;
    public List<Challenge> items = new ArrayList<Challenge>();

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
           // Log.d("Challenges Fetch", "initiated");
            getDealsHistory();
        }
    }

    private void getDealsHistory() {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse<List<Challenge>>> callback = apiService.getDealsHistory(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<List<Challenge>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Challenge>>> response) {
                problem.setVisibility(View.GONE);
                hideLoader(progress);
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(getActivity(), user);
                                getDealsHistory();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                if (response.body().getValues() != null && response.body().getValues().size() > 0) {
                                    items = response.body().getValues();
                                    //Log.d("deals his resp", new Gson().toJson(items));
                                    drawItems();
                                } else {
                                    setProblem(getResources().getString(R.string.user_deal_frag_prob_three), "3");
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
            dealsHistoryRecyclerViewAdapter = new DealsHistoryRecyclerViewAdapter(items, getActivity());
            recyclerListItems.setAdapter(dealsHistoryRecyclerViewAdapter);
            recyclerListItems.setVisibility(View.VISIBLE);
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
                getDealsHistory();
            } else if (error.getTag().toString().equalsIgnoreCase("3")) {
                makeViewsGone();
                getDealsHistory();
            }
        }
    }

    private void makeViewsGone() {
        problem.setVisibility(View.GONE);
        recyclerListItems.setVisibility(View.GONE);
    }
}
