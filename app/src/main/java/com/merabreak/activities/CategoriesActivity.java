package com.merabreak.activities;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.CustomListAdapter;
import com.merabreak.MarginDecoration;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.adapter.CategoryRecyclerViewAdapter;
import com.merabreak.models.APIResponse;
import com.merabreak.models.challenge.Category;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by Ewebcore on 05-Feb-16.
 */
@EActivity(R.layout.categories)
public class CategoriesActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.progress)
    LinearLayout progress;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.error)
    TextView error;

    @ViewById(R.id.empty)
    LinearLayout empty;

    @ViewById(R.id.items)
    RecyclerView recyclerViewCategories;

    private CustomListAdapter challengesAdapter = null;
    CategoryRecyclerViewAdapter categoryRecyclerViewAdapter;

    List<Category> items = new ArrayList<Category>();

    @AfterViews
    void init() {
        empty.setVisibility(View.GONE);
        title.setText(R.string.categories_title);
        toolbar.setNavigationIcon(R.drawable.button_back1);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());
        getCategories();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getCategories() {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse<List<Category>>> callback = apiService.getCategories(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<List<Category>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Category>>> response) {
                hideLoader(progress);
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(CategoriesActivity.this, user);
                                getCategories();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                if (response.body().getValues() != null && response.body().getValues().size() > 0) {
                                    items = response.body().getValues();
                                    drawCategories();
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
                }else{
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

    private void drawCategories() {
        recyclerViewCategories.setLayoutManager(new GridLayoutManager(CategoriesActivity.this, 3));
        categoryRecyclerViewAdapter = new CategoryRecyclerViewAdapter(items, this);
        recyclerViewCategories.addItemDecoration(new MarginDecoration(this));
        recyclerViewCategories.setAdapter(categoryRecyclerViewAdapter);
        ((CategoryRecyclerViewAdapter) categoryRecyclerViewAdapter).setOnItemClickListener(new CategoryRecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                //Log.i("Item Click", " Clicked on Item " + position);
                items.get(position);
                ChallengesActivity_.intent(CategoriesActivity.this).category(items.get(position)).start();
            }
        });
    }
}
