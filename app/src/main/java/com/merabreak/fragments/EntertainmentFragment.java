package com.merabreak.fragments;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.merabreak.ApplicationSingleton;
import com.merabreak.BaseFragment;
import com.merabreak.Constants;
import com.merabreak.EndlessRecyclerViewNestedScrollListener;
import com.merabreak.GoogleAnalyticEvents;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.activities.ArticleActivity_;
import com.merabreak.activities.ChallengeDetailActivity_;
import com.merabreak.activities.ChallengesActivity;
import com.merabreak.activities.OfflineChallengesActivity_;
import com.merabreak.adapter.ChallengeRecyclerViewAdapter;
import com.merabreak.models.APIResponse;
import com.merabreak.models.challenge.Challenge;
import com.merabreak.network.Connectivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by ETPL-002 on 20-Sep-17.
 */
@EFragment(R.layout.home_challenges)
public class EntertainmentFragment extends BaseFragment {

    @ViewById(R.id.items)
    RecyclerView recyclerListItems;

    private ChallengeRecyclerViewAdapter recyclerListItemsAdapter;

    public List<Challenge> items = new ArrayList<Challenge>();

    @ViewById(R.id.problem)
    LinearLayout problem;

    @ViewById(R.id.error)
    TextView error;

    @ViewById(R.id.retry)
    Button retry;

    @ViewById(R.id.linearMain)
    NestedScrollView linearMain;

    @ViewById(R.id.progress_bar)
    ProgressBar progressBar;

    int pages = 2, limit = 20;

    @AfterViews
    void init() {
        if (!Connectivity.isConnected(getActivity())) {
            makeViewsGone();
            setProblem(context.getResources().getString(R.string.no_internet), "1");
        } else {
            makeViewsGone();
            getChallenges();
        }
        GridLayoutManager manager1 = new GridLayoutManager(getActivity(), 2);
        manager1.setReverseLayout(false);
        manager1.setStackFromEnd(false);
        recyclerListItems.setLayoutManager(manager1);
        recyclerListItems.setNestedScrollingEnabled(false);
        recyclerListItems.setHasFixedSize(false);
        linearMain.setOnScrollChangeListener(new EndlessRecyclerViewNestedScrollListener(manager1) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
               // Log.d("##### set on", String.valueOf(page + " $ " + String.valueOf(totalItemsCount)));

                if (!Connectivity.isConnected(getActivity())) {
                    Util.makeToast(context, context.getResources().getString(R.string.no_internet));
                } else {
                    getChallengesPagination(pages);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getChallenges(){
        Call<APIResponse<List<Challenge>>> callback = apiService.getChallengesAsPerCategoryPagination(Constants.MB_API_KEY, user.getAuthToken(), "entertainment", 1, limit, Util.getDeviceDPI(context));
        callback.enqueue(new Callback<APIResponse<List<Challenge>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Challenge>>> response) {
                progressBar.setVisibility(View.GONE);
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(getActivity(), user);
                                getChallenges();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                if (response.body().getValues() != null) {
                                    items = response.body().getValues();
                                    drawChallenges();
                                } else {
                                    setProblem(response.body().getMeta().getMessage(), "3");
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
                Log.d("Error msg pagination", "pagination api failure");
            }
        });

    }

    private void getChallengesPagination(int page){
        progressBar.setVisibility(View.VISIBLE);
        Call<APIResponse<List<Challenge>>> callback = apiService.getChallengesAsPerCategoryPagination(Constants.MB_API_KEY, user.getAuthToken(), "entertainment", page, limit, Util.getDeviceDPI(context));
        callback.enqueue(new Callback<APIResponse<List<Challenge>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Challenge>>> response) {
                progressBar.setVisibility(View.GONE);
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(getActivity(), user);
                                getChallengesPagination(page);
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getValues() != null) {
                                List<Challenge> ch = response.body().getValues();
                                for (int i = 0; i < ch.size(); i++) {
                                    Challenge cha = ch.get(i);
                                    recyclerListItemsAdapter.mDataset.add(cha);
                                }
                                recyclerListItemsAdapter.notifyDataSetChanged();
                                pages++;
                            } else {
                                Util.makeToast(getActivity(), response.body().getMeta().getMessage());
                            }
                        }
                    } else {
                        Util.makeToast(getActivity(), Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(getActivity(), Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d("Error msg pagination", "pagination api failure");
            }
        });
    }

    private void drawChallenges(){
        if (getActivity() != null) {

            if (items.size() > 0)
                recyclerListItems.setVisibility(View.VISIBLE);

            recyclerListItemsAdapter = new ChallengeRecyclerViewAdapter(items, getActivity(), context);
            recyclerListItems.setAdapter(recyclerListItemsAdapter);
            ((ChallengeRecyclerViewAdapter) recyclerListItemsAdapter).setOnItemClickListener(new ChallengeRecyclerViewAdapter.MyClickListener() {
                @Override
                public void onItemClick(Challenge position, View v) {
                    if(position.getChallengeType().equalsIgnoreCase("article")){
                        ArticleActivity_.intent(context).challenge(position).start();
                    }else {
                        //Log.i("Item Click", " Clicked on Item " + position);
                        GoogleAnalyticEvents.event(getActivity(), gaTracker, "Challenge Click", "Challenge Clicked", user.getFullName() + ": " + user.getPhone() + ": " + position.getTitle());
                       /* Map<String, Object> eventValue = new HashMap<String, Object>();
                        eventValue.put(AFInAppEventParameterName.PARAM_3, position.getTitle());
                        ((ApplicationSingleton) context.getApplicationContext()).appsFlyerLib().trackEvent(getActivity(), "Challenge Clicked", eventValue);*/
                        ChallengeDetailActivity_.intent(context).challenge(position).start();
                    }
                }

                @Override
                public void onLongItemClick(Challenge position, View v) {
                }

                @Override
                public void onOfflineClick(Challenge position, View v) {

                }
            });
        }
        if (items.size() > 0)
            problem.setVisibility(View.GONE);
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

    @Click(R.id.offline_challenges)
    void playOffline() {
        OfflineChallengesActivity_.intent(getActivity()).start();
    }

    @Click(R.id.retry)
    void onRetryClick() {
        if (error.getTag() != null) {
            if (error.getTag().toString().equalsIgnoreCase("1")) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
            } else if (error.getTag().toString().equalsIgnoreCase("2")) {
                makeViewsGone();
                getChallenges();
            } else if (error.getTag().toString().equalsIgnoreCase("3")) {
                makeViewsGone();
                getChallenges();
            }
        }
    }

    private void makeViewsGone() {
        problem.setVisibility(View.GONE);
        recyclerListItems.setVisibility(View.GONE);
    }
}
