package com.merabreak.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.merabreak.BaseFragment;
import com.merabreak.Constants;
import com.merabreak.FontUtils;
import com.merabreak.MainActivity;
import com.merabreak.NavigationItemType;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.activities.ChallengesActivity;
import com.merabreak.activities.ChallengesActivity_;
import com.merabreak.adapter.CategoryRecyclerViewAdapter;
import com.merabreak.models.APIResponse;
import com.merabreak.models.challenge.Category;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import rx.Observable;
import rx.subjects.PublishSubject;


@EFragment(R.layout.challenge_drawer)
public class ChallengesDrawerFragment extends BaseFragment {
    private PublishSubject<NavigationItemType> navigationItemSelection = PublishSubject.create();
    @ViewById(R.id.items)
    RecyclerView recyclerViewCategories;

    CategoryRecyclerViewAdapter categoryRecyclerViewAdapter;

    List<Category> items = new ArrayList<Category>();

    @ViewById(R.id.error)
    TextView error;

    @ViewById(R.id.empty)
    LinearLayout empty;

    @ViewById(R.id.menus)
    LinearLayout menus;
    private int selectedPos = -1;

    @FragmentArg
    Category category;

    @AfterViews
    void init() {

        items = Util.getCategories(getActivity());
        if (items != null && items.size() > 0) {
            empty.setVisibility(View.GONE);
            drawMenus();
        } else {
            getCategories();
        }
    }

    private void getCategories() {
        Call<APIResponse<List<Category>>> callback = apiService.getCategories(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<List<Category>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Category>>> response) {
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(getActivity(), user);
                                getCategories();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                if (response.body().getValues() != null && response.body().getValues().size() > 0) {
                                    Util.saveCategories(getActivity(), response.body().getValues());
                                    drawMenus();
                                    empty.setVisibility(View.GONE);
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
                } else {
                    empty.setVisibility(View.VISIBLE);
                    error.setText(Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                empty.setVisibility(View.VISIBLE);
                error.setText(Constants.SOME_THING_WRONG);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void drawMenus() {
        items = Util.getCategories(getActivity());

        for (int i = 0; i < items.size(); i++) {
            Category item = items.get(i);

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_category1, null);
            Button check = (Button) view.findViewById(R.id.check1);

            if (category != null) {
                if (category.getTitle().equalsIgnoreCase(item.getTitle())) {
                    selectedPos = i;
                    check.setSelected(true);
                }
            }

            check.setText(item.getTitle());
            check.setTag(i + "");

            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getTag() != null) {
                        int pos = Integer.parseInt(v.getTag().toString());
                        if (selectedPos == -1) {
                            selectedPos = pos;
                            v.setSelected(true);
                            ((ChallengesActivity) getActivity()).closeDrawer();
                            ((ChallengesActivity) getActivity()).getChallengesAsPerCategory(items.get(pos));
                        } else {
                            if (selectedPos == pos) {
                                ((ChallengesActivity) getActivity()).closeDrawer();
                            } else {
                                deselectAnswers();
                                v.setSelected(true);
                                selectedPos = pos;
                                ((ChallengesActivity) getActivity()).closeDrawer();
                                ((ChallengesActivity) getActivity()).getChallengesAsPerCategory(items.get(pos));
                            }
                        }
                    }
                }
            }
            );
            menus.addView(view);
        }
    }

    private void deselectAnswers() {
        for (int i = 0; i < menus.getChildCount(); i++) {
            View answerView = menus.getChildAt(i);
            Button check = (Button) answerView.findViewById(R.id.check1);
            check.setSelected(false);
        }
    }
}
