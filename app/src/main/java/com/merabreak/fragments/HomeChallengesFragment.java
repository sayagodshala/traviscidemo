package com.merabreak.fragments;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.merabreak.ApplicationSingleton;
import com.merabreak.BaseFragment;
import com.merabreak.Constants;
import com.merabreak.EndlessRecyclerViewNestedScrollListener;
import com.merabreak.GoogleAnalyticEvents;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.activities.ArticleActivity_;
import com.merabreak.activities.ChallengeDetailActivity_;
import com.merabreak.activities.OfflineChallengesActivity_;
import com.merabreak.adapter.ChallengeRecyclerViewAdapter;
import com.merabreak.adapter.FeaturedChallengeSliderView;
import com.merabreak.models.APIResponse;
import com.merabreak.models.HomeCategory;
import com.merabreak.models.PaginationChallenges;
import com.merabreak.models.challenge.Challenge;
import com.merabreak.network.Connectivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;


@EFragment(R.layout.home_challenges)
public class HomeChallengesFragment extends BaseFragment implements ViewPagerEx.OnPageChangeListener {

    @ViewById(R.id.items)
    RecyclerView recyclerListItems;

    private ChallengeRecyclerViewAdapter recyclerListItemsAdapter;

    public List<Challenge> items = new ArrayList<Challenge>();

    public List<Challenge> fc = new ArrayList<Challenge>();

    @ViewById(R.id.slider)
    SliderLayout slider;

    @ViewById(R.id.banner)
    RelativeLayout banner;

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

    @ViewById(R.id.custom_indicator)
    PagerIndicator customIndicator;

    int pages = 2, limit = 20;

    @FragmentArg
    HomeCategory homeCategory;

    @AfterViews
    void init() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        manager.setStackFromEnd(false);
        GridLayoutManager manager1 = new GridLayoutManager(getActivity(), 2);
        manager1.setReverseLayout(false);
        manager1.setStackFromEnd(false);
        recyclerListItems.setLayoutManager(manager1);
        recyclerListItems.setNestedScrollingEnabled(false);
        recyclerListItems.setHasFixedSize(false);
        items = homeCategory.getChallenges();
        fc = homeCategory.getFeaturedChallenge();

        linearMain.setOnScrollChangeListener(new EndlessRecyclerViewNestedScrollListener(manager1) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
               // Log.d("##### set on", String.valueOf(page + " $ " + String.valueOf(totalItemsCount)));

                if (!Connectivity.isConnected(getActivity())) {
                    Util.makeToast(context, context.getResources().getString(R.string.no_internet));
                } else {
                    getpages(pages);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        makeViewsGone();
        if (!Connectivity.isConnected(getActivity())) {
            setProblem(context.getResources().getString(R.string.no_internet), "1");
        } else {
            drawItems();
        }
    }

    private void initBanner() {
        if (slider != null)
            slider.removeAllSliders();
        //  banner.setVisibility(View.VISIBLE);
        for (int i = 0; i < fc.size(); i++) {
            final Challenge challenge = fc.get(i);
            FeaturedChallengeSliderView featuredChallengeSliderView = new FeaturedChallengeSliderView(getActivity());
            Challenge tempChallenge = new Challenge(challenge.getTitle(), challenge.getTotalPlayed(), challenge.getChallengeTitleColor(),challenge.getCategoryName(), challenge.getCategoryColor(), challenge.getContentLanguage());
            featuredChallengeSliderView.description(new Gson().toJson(tempChallenge)).
                    image(challenge.getCoverImage()).setScaleType(BaseSliderView.ScaleType.CenterCrop).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView baseSliderView) {
                    ChallengeDetailActivity_.intent(context).challenge(challenge).start();
                }
            });
            slider.addSlider(featuredChallengeSliderView);
        }
        if (fc.size() > 1) {
            slider.setPresetTransformer(SliderLayout.Transformer.Default);
            slider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Visible);
            slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            slider.setCustomIndicator(customIndicator);
            slider.setDuration(4000);
            slider.addOnPageChangeListener(this);
        } else {
            slider.stopAutoCycle();
            slider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
            customIndicator.setVisibility(View.GONE);
            customIndicator.destroySelf();
            slider.setEnabled(false);
            slider.setFocusable(false);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onStop() {
        if (slider != null) {
            slider.stopAutoCycle();
        }
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    void getpages(int page) {
        progressBar.setVisibility(View.VISIBLE);
        Call<APIResponse<PaginationChallenges>> callback = apiService.getPages(Constants.MB_API_KEY, user.getAuthToken(), homeCategory.getName(), page, limit, Util.getDeviceDPI(context));
        callback.enqueue(new Callback<APIResponse<PaginationChallenges>>() {
            @Override
            public void onResponse(Response<APIResponse<PaginationChallenges>> response) {
                progressBar.setVisibility(View.GONE);
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(getActivity(), user);
                                getpages(page);
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getValues() != null) {
                                PaginationChallenges items = response.body().getValues();
                                List<Challenge> ch = items.getChallenges();
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
                hideLoader();
            }
        });
    }

    private void drawItems() {
        if (getActivity() != null) {

            if (items.size() > 0)
                recyclerListItems.setVisibility(View.VISIBLE);

           /* if (fc.size() > 0)
                initBanner();
            else
                banner.setVisibility(View.GONE);*/

            recyclerListItemsAdapter = new ChallengeRecyclerViewAdapter(items, getActivity(), context);
            recyclerListItems.setAdapter(recyclerListItemsAdapter);
            ((ChallengeRecyclerViewAdapter) recyclerListItemsAdapter).setOnItemClickListener(new ChallengeRecyclerViewAdapter.MyClickListener() {
                @Override
                public void onItemClick(Challenge position, View v) {
                    //Log.i("Item Click", " Clicked on Item " + position);
                    if(position.getChallengeType().equalsIgnoreCase("article")){
                        ArticleActivity_.intent(context).challenge(position).start();
                    }else {
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

            } else if (error.getTag().toString().equalsIgnoreCase("3")) {

            }
        }
    }

    private void makeViewsGone() {
        problem.setVisibility(View.GONE);
        recyclerListItems.setVisibility(View.GONE);
        banner.setVisibility(View.GONE);
    }
}