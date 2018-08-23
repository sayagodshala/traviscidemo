package com.merabreak.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.merabreak.ApplicationSingleton;
import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.EndlessRecyclerViewScrollListener;
import com.merabreak.Fragments;
import com.merabreak.GoogleAnalyticEvents;
import com.merabreak.NavigationItemType;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.adapter.ChallengeRecyclerViewAdapter;
import com.merabreak.controls.DividerItemDecoration;
import com.merabreak.fragments.ChallengesDrawerFragment;
import com.merabreak.fragments.ChallengesDrawerFragment_;
import com.merabreak.models.APIResponse;
import com.merabreak.models.challenge.Category;
import com.merabreak.models.challenge.Challenge;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import rx.Observable;

/**
 * Created by Ewebcore on 05-Feb-16.
 */
@EActivity(R.layout.challenges)
public class ChallengesActivity extends BaseActivity {

    private static final String NAVIGATION_DRAWER_TAG = "NAVIGATION_DRAWER_TAG2";

    @ViewById(R.id.drawer)
    DrawerLayout drawer;

    @ViewById(R.id.progress)
    LinearLayout progress;

    ActionBarDrawerToggle drawerToggle;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.hamburger_icon)
    ImageView toolbarRightIcon;

    @ViewById(R.id.error)
    TextView error;

    @ViewById(R.id.empty)
    LinearLayout empty;

    @ViewById(R.id.items)
    RecyclerView recyclerListItems;

    private ChallengeRecyclerViewAdapter recyclerListItemsAdapter;

    @ViewById(R.id.progress_bar)
    ProgressBar progressBar;

    public List<Challenge> items = new ArrayList<Challenge>();

    @Extra
    Category category;

    @Extra
    String filterType;

    List<String> downloadURLs = new ArrayList<>();

    private int totalCounts = 0;
    private int finalCounts = 0;
    int pages = 1, limit = 15, page = 2;

    @AfterViews
    void init() {
        empty.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.mb_back_icon);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());
        toolbarRightIcon.setVisibility(View.VISIBLE);

        if (category != null && !category.getSlug().equalsIgnoreCase("")) {
            getChallengesAsPerCategory(null);
            title.setText(category.getTitle());
        } else {
            //   getFilteredChallenges(filterType.toLowerCase());
            getChallengesPaginationData(filterType.toLowerCase());
            title.setText(filterType);
        }

        drawerToggle =
                new ActionBarDrawerToggle(this, drawer, null, R.string.app_name, R.string.app_name);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawer.setDrawerListener(drawerToggle);
        recyclerListItems.setVisibility(View.VISIBLE);
        // LinearLayoutManager manager = new LinearLayoutManager(this);
        // manager.setOrientation(LinearLayoutManager.VERTICAL);
        GridLayoutManager manager1 = new GridLayoutManager(this, 2);
        recyclerListItems.setLayoutManager(manager1);
        recyclerListItems.setNestedScrollingEnabled(false);
        recyclerListItems.setHasFixedSize(false);
        recyclerListItems.addOnScrollListener(new EndlessRecyclerViewScrollListener(manager1) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (category != null && !category.getSlug().equalsIgnoreCase("")) {
                    getChallengesAsPerCategoryPagination();
                }
            }
        });
        loadDrawer();
    }

    private void getChallengesAsPerCategoryPagination() {
        progressBar.setVisibility(View.VISIBLE);
        Call<APIResponse<List<Challenge>>> callback = apiService.getChallengesAsPerCategoryPagination(Constants.MB_API_KEY, user.getAuthToken(), category.getSlug(), page, limit, Util.getDeviceDPI(this));
        callback.enqueue(new Callback<APIResponse<List<Challenge>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Challenge>>> response) {
                progressBar.setVisibility(View.GONE);
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().isStatus() && response.body().getValues() != null && response.body().getValues().size() > 0) {
                                // this is for pagination data
                                List<Challenge> items = response.body().getValues();
                                for (int i = 0; i < items.size(); i++) {
                                    Challenge challenge = items.get(i);
                                    recyclerListItemsAdapter.mDataset.add(challenge);
                                }
                                recyclerListItemsAdapter.notifyDataSetChanged();
                                page++;
                        } else {
                            empty.setVisibility(View.GONE);
                            error.setText(response.body().getMeta().getMessage());
                        }
                    } else {
                        empty.setVisibility(View.VISIBLE);
                        error.setText(Constants.SOME_THING_WRONG);
                    }
                }else{
                    empty.setVisibility(View.VISIBLE);
                    error.setText(Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getChallengesPaginationData(String type) {
        progressBar.setVisibility(View.VISIBLE);
        Call<APIResponse<List<Challenge>>> callback = apiService.getFilteredChallengesPagination(Constants.MB_API_KEY, user.getAuthToken(), type, pages, limit, Util.getDeviceDPI(this));
        callback.enqueue(new Callback<APIResponse<List<Challenge>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Challenge>>> response) {
                progressBar.setVisibility(View.GONE);
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(ChallengesActivity.this, user);
                                getChallengesPaginationData(type);
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus() && response.body().getValues() != null && response.body().getValues().size() > 0) {
                                    items = response.body().getValues();
                                    if (pages == 1) {  // this is for default page data
                                        if (response.body().getValues() != null && response.body().getValues().size() > 0) {
                                            drawChallenges();
                                        } else {
                                            empty.setVisibility(View.VISIBLE);
                                            error.setText(response.body().getMeta().getMessage());
                                        }

                                    } else {      // this is for pagination data
                                        List<Challenge> items = response.body().getValues();
                                        for (int i = 0; i < items.size(); i++) {
                                            Challenge challenge = items.get(i);
                                            recyclerListItemsAdapter.mDataset.add(challenge);
                                        }
                                        recyclerListItemsAdapter.notifyDataSetChanged();
                                    }
                                    pages++;
                            } else {
                                empty.setVisibility(View.GONE);
                                error.setText(response.body().getMeta().getMessage());
                            }
                        }
                    } else {
                        empty.setVisibility(View.VISIBLE);
                        error.setText(Constants.SOME_THING_WRONG);
                    }
                }else{
                    empty.setVisibility(View.VISIBLE);
                    error.setText(Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            closeDrawer();
            return;
        }
        toolbarRightIcon.setVisibility(View.GONE);
        super.onBackPressed();
    }

    private void loadDrawer() {
        ChallengesDrawerFragment drawer = ChallengesDrawerFragment_.builder().category(category).build();
        Fragments.replaceContentFragment(this, R.id.drawer_container, drawer, NAVIGATION_DRAWER_TAG);
    }

    private void toggleDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            closeDrawer();
        } else {
            openDrawer();
        }
    }

    private void openDrawer() {
        drawer.openDrawer(GravityCompat.END);
    }

    public void closeDrawer() {
        drawer.closeDrawer(GravityCompat.END);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void getChallengesAsPerCategory(Category cat) {
        if (cat != null)
            category = cat;

        title.setText(category.getTitle());
        items = new ArrayList<>();
        recyclerListItems.setVisibility(View.GONE);
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse<List<Challenge>>> callback = apiService.getChallengesAsPerCategory(Constants.MB_API_KEY, user.getAuthToken(), category.getSlug(), Util.getDeviceDPI(this));
        callback.enqueue(new Callback<APIResponse<List<Challenge>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Challenge>>> response) {
                hideLoader(progress);
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(ChallengesActivity.this, user);
                                getChallengesAsPerCategory(cat);
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus() && response.body().getValues() != null && response.body().getValues().size() > 0) {
                                    //Log.d("Values Size", response.body().getValues().size() + "");
                                    items = response.body().getValues();
                                    drawChallenges();
                            } else {
                                empty.setVisibility(View.GONE);
                                error.setText(response.body().getMeta().getMessage());
                            }
                        }
                    } else {
                        empty.setVisibility(View.VISIBLE);
                        error.setText(Constants.SOME_THING_WRONG);
                    }
                }else{
                    empty.setVisibility(View.VISIBLE);
                    error.setText(Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                error.setText(Constants.SOME_THING_WRONG);
                empty.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getFilteredChallenges(String type) {
        recyclerListItems.setVisibility(View.GONE);
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse<List<Challenge>>> callback = apiService.getFilteredChallenges(Constants.MB_API_KEY, user.getAuthToken(), type);

//        String latLng = Util.getLatLng(this);
//        if (!latLng.equalsIgnoreCase("")) {
//            String[] coords = latLng.split(",");
//            if (coords.length > 0)
//                callback = apiService.getPopularChallenges(Constants.MB_API_KEY, user.getAuthToken(), coords[0], coords[1]);
//        }

        callback.enqueue(new Callback<APIResponse<List<Challenge>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Challenge>>> response) {
                hideLoader(progress);
                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().isStatus()) {
                        if (response.body().getValues() != null && response.body().getValues().size() > 0) {
                            items = response.body().getValues();
                            drawChallenges();
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
            }
        });
    }

    private void drawChallenges() {

        recyclerListItems.setVisibility(View.VISIBLE);
        recyclerListItemsAdapter = new ChallengeRecyclerViewAdapter(items, this, this);
        recyclerListItems.setAdapter(recyclerListItemsAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);

        ((ChallengeRecyclerViewAdapter) recyclerListItemsAdapter).setOnItemClickListener(new ChallengeRecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(Challenge position, View v) {
                //Log.i("Item Click", " Clicked on Item " + position);
                if(position.getChallengeType().equalsIgnoreCase("article")){
                        ArticleActivity_.intent(ChallengesActivity.this).challenge(position).start();
                }else {
                    GoogleAnalyticEvents.event(ChallengesActivity.this, gaTracker, "Challenge Click", "User Refered the Challenge Details", user.getFullName() + ": " + user.getPhone() + ": " + position.getTitle());
                   /* Map<String, Object> eventValue = new HashMap<String, Object>();
                    eventValue.put(AFInAppEventParameterName.PARAM_3, position.getTitle());
                    ((ApplicationSingleton) ChallengesActivity.this.getApplication()).appsFlyerLib().trackEvent(ChallengesActivity.this, "Challenge Clicked", eventValue);*/
                    ChallengeDetailActivity_.intent(ChallengesActivity.this).challenge(position).start();
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

    @Click(R.id.hamburger_icon)
    void openRightDrawer(){
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            closeDrawer();
        } else {
            openDrawer();
        }
    }
}
