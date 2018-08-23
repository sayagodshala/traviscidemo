package com.merabreak.fragments;


import android.widget.LinearLayout;
import android.widget.ListView;

import com.merabreak.BaseFragment;
import com.merabreak.Constants;
import com.merabreak.CustomListAdapter;
import com.merabreak.R;
import com.merabreak.RaffleItem;
import com.merabreak.RaffleItem_;
import com.merabreak.Util;
import com.merabreak.activities.RaffleActivity_;
import com.merabreak.models.APIResponse;
import com.merabreak.models.Raffle;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

@EFragment(R.layout.raffles)
public class RafflesFragment extends BaseFragment {


    @ViewById(R.id.items)
    ListView items;

    @ViewById(R.id.progress)
    LinearLayout progress;

    private CustomListAdapter itemsAdapter = null;

    @AfterViews
    void init() {
        getRaffles();
    }

    private void getRaffles() {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse<List<Raffle>>> callback = apiService.getRaffles(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<List<Raffle>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Raffle>>> response) {
                hideLoader(progress);
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().isStatus()) {
                            setRaffles(response.body().getValues());
                        } else {
                            Util.makeToast(getActivity(), response.body().getMeta().getMessage());
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
                hideLoader(progress);
                if (t.getMessage() != null)
                    Util.makeToast(getActivity(), t.getMessage());
            }
        });
    }


    private void setRaffles(List<Raffle> raffles) {
        itemsAdapter = new CustomListAdapter<>(raffles, this::raffleItemView);
        items.setAdapter(itemsAdapter);
    }

    private RaffleItem raffleItemView() {
        RaffleItem raffleItem = RaffleItem_.build(getActivity());
        return raffleItem;
    }

    @ItemClick(R.id.items)
    void onItemClick(Raffle raffle) {
        RaffleActivity_.intent(getActivity()).id(raffle.getSlug()).start();
    }
}
