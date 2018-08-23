package com.merabreak.fragments;

import android.widget.ListView;

import com.merabreak.BaseFragment;
import com.merabreak.CustomListAdapter;
import com.merabreak.R;
import com.merabreak.models.challenge.Challenge;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EFragment(R.layout.challenges)
public class ChallengesFragment extends BaseFragment {

    @ViewById(R.id.items)
    ListView items;

    private CustomListAdapter itemsAdapter = null;

    List<Challenge> challenges;
    Challenge challenge;

    @AfterViews
    void init() {

    }


    @Override
    public void onResume() {
        super.onResume();
    }
}