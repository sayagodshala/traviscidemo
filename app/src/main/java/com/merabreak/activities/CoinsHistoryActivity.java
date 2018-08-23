package com.merabreak.activities;

import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.merabreak.BaseActivity;
import com.merabreak.CoinItem;
import com.merabreak.CoinItem_;
import com.merabreak.Constants;
import com.merabreak.CustomListAdapter;
import com.merabreak.R;
import com.merabreak.models.APIResponse;
import com.merabreak.models.Coin;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by Ewebcore on 05-Feb-16.
 */
@EActivity(R.layout.challenges)
public class CoinsHistoryActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.progress)
    LinearLayout progress;

    @ViewById(R.id.error)
    TextView error;

    @ViewById(R.id.items)
    ListView items;

    private CustomListAdapter coinsAdapter = null;

    List<Coin> coins;

    @AfterViews
    void init() {
        title.setText(R.string.points_history_title);
        toolbar.setNavigationIcon(R.drawable.button_back1);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());
        getCoinsHistory();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getCoinsHistory() {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse<List<Coin>>> callback = apiService.getUserCoinsHistory(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<List<Coin>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Coin>>> response) {
                hideLoader(progress);
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().isStatus()) {
                            if (response.body().getValues() != null && response.body().getValues().size() > 0) {
                                coins = response.body().getValues();
                                drawItems();
                            } else {
                                error.setText(R.string.points_history_empty);
                            }
                        } else {
                            error.setText(response.body().getMeta().getMessage());
                        }
                    } else {
                        error.setText(Constants.SOME_THING_WRONG);
                    }
                } else {
                    error.setText(Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                error.setText(Constants.SOME_THING_WRONG);
            }
        });
    }

    private void drawItems() {
        coinsAdapter = new CustomListAdapter<>(coins, this::coinItemView);
        items.setAdapter(coinsAdapter);
    }

    private CoinItem coinItemView() {
        CoinItem item = CoinItem_.build(this);
        return item;
    }
}
