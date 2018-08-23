package com.merabreak.fragments;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.merabreak.BaseFragment;
import com.merabreak.Constants;
import com.merabreak.CustomListAdapter;
import com.merabreak.EndlessRecyclerViewNestedScrollListener;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.adapter.UserChallengeRecyclerViewAdapter;
import com.merabreak.itemViews.ChallengeItemView;
import com.merabreak.itemViews.ChallengeItemView_;
import com.merabreak.models.APIResponse;
import com.merabreak.models.challenge.Challenge;
import com.merabreak.network.Connectivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;


@EFragment(R.layout.user_challenges)
public class UserChallengesFragment extends BaseFragment {

    @ViewById(R.id.items)
    RecyclerView recyclerListItems;

    @ViewById(R.id.challenge_history_main)
    NestedScrollView challenge_history_main;

    @ViewById(R.id.progress_bar)
    ProgressBar progressBar;

    private RecyclerView.LayoutManager mLayoutManager;
    private UserChallengeRecyclerViewAdapter recyclerListItemsAdapter;

    public List<Challenge> items = new ArrayList<Challenge>();

    @ViewById(R.id.problem)
    LinearLayout problem;

    @ViewById(R.id.error)
    TextView error;

    @ViewById(R.id.retry)
    Button retry;

    private CustomListAdapter itemsAdapter = null;

    int pages = 2, limit = 15;

    @AfterViews
    void init() {
        if (!Connectivity.isConnected(getActivity())) {
            makeViewsGone();
            setProblem(getResources().getString(R.string.no_internet), "1");
        } else {
            makeViewsGone();
            //Log.d("Challenges Fetch", "initiated");
            getCompletedChallenges();
        }

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        GridLayoutManager manager1 = new GridLayoutManager(getActivity(), 2);
        manager1.setReverseLayout(false);
        manager1.setStackFromEnd(false);
        recyclerListItems.setLayoutManager(manager1);
        recyclerListItems.setNestedScrollingEnabled(false);
        recyclerListItems.setHasFixedSize(false);

        challenge_history_main.setOnScrollChangeListener(new EndlessRecyclerViewNestedScrollListener(manager1) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getCompletedChallengesPagination(pages);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getCompletedChallenges() {
        progressBar.setVisibility(View.VISIBLE);
        Call<APIResponse<List<Challenge>>> callback = apiService.getCompletedChallenges(Constants.MB_API_KEY, user.getAuthToken(), 1, limit);
        callback.enqueue(new Callback<APIResponse<List<Challenge>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Challenge>>> response) {
                problem.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                if(response.body() != null && response.code() == 200) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(getActivity(), user);
                                getCompletedChallenges();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                if (response.body().getValues() != null && response.body().getValues().size() > 0) {

                                    items = response.body().getValues();
                                    drawChallenges();

                                } else {
                                    setProblem(getResources().getString(R.string.user_chal_frag_prob_three), "3");
                                }
                            } else {
                                setProblem(response.body().getMeta().getMessage(), "2");
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
                setProblem(Constants.SOME_THING_WRONG, "2");
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getCompletedChallengesPagination(int page) {
        progressBar.setVisibility(View.VISIBLE);
        Call<APIResponse<List<Challenge>>> callback = apiService.getCompletedChallenges(Constants.MB_API_KEY, user.getAuthToken(), page, limit);
        callback.enqueue(new Callback<APIResponse<List<Challenge>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Challenge>>> response) {
                problem.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                if(response.body() != null && response.code() == 200) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(getActivity(), user);
                                getCompletedChallengesPagination(page);
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                if (response.body().getValues() != null && response.body().getValues().size() > 0) {

                                    List<Challenge> items = response.body().getValues();
                                    for (int i = 0; i < items.size(); i++) {
                                        Challenge challenge = items.get(i);
                                        recyclerListItemsAdapter.mDataset.add(challenge);
                                    }
                                    recyclerListItemsAdapter.notifyDataSetChanged();
                                    pages++;
                                } else {
                                    Log.d("chall his pagination", response.body().getMeta().getMessage());
                                    // setProblem(response.body().getMeta().getMessage(), "3");
                                }
                            } else {
                                Log.d("chall his pagination1", response.body().getMeta().getMessage());
                                //  setProblem(response.body().getMeta().getMessage(), "2");
                            }
                        }
                    } else {
                        Log.d("chall his pagination2", Constants.SOME_THING_WRONG);
                        Util.makeToast(getActivity(), Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(getActivity(), Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                setProblem(Constants.SOME_THING_WRONG, "2");
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void drawChallenges() {
        if (context != null) {
            problem.setVisibility(View.GONE);
            recyclerListItemsAdapter = new UserChallengeRecyclerViewAdapter(items, getActivity());
            recyclerListItems.setAdapter(recyclerListItemsAdapter);
            recyclerListItems.setVisibility(View.VISIBLE);
            ((UserChallengeRecyclerViewAdapter) recyclerListItemsAdapter).setOnItemClickListener(new UserChallengeRecyclerViewAdapter.MyClickListener() {
                @Override
                public void onItemClick(int position, View v) {

                }

                @Override
                public void onLongItemClick(int position, View v) {

                }

                @Override
                public void onOfflineClick(int position, View v) {

                }
            });
        }
    }

    private ChallengeItemView challengeItemView() {
        ChallengeItemView item = ChallengeItemView_.build(getActivity());
        return item;
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
                getCompletedChallenges();
            } else if (error.getTag().toString().equalsIgnoreCase("3")) {
                makeViewsGone();
                getCompletedChallenges();
            }
        }
    }

    private void makeViewsGone() {
        problem.setVisibility(View.GONE);
        recyclerListItems.setVisibility(View.GONE);
    }
}