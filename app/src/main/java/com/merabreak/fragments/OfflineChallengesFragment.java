package com.merabreak.fragments;

import android.widget.ListView;
import android.widget.TextView;

import com.merabreak.BaseFragment;
import com.merabreak.CustomListAdapter;
import com.merabreak.R;
import com.merabreak.models.challenge.Challenge;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EFragment(R.layout.offline_challenges)
public class OfflineChallengesFragment extends BaseFragment {

    @ViewById(R.id.items)
    ListView items;

    @ViewById(R.id.error)
    TextView error;

    private CustomListAdapter itemsAdapter = null;

    List<Challenge> challenges;

    @AfterViews
    void init() {
//        setOfflineChallenges();
    }


//    @ItemClick(R.id.items)
//    void onItemClick(Challenge challenge) {
//        Log.d("Offline Challenge", new Gson().toJson(challenge));
//        ChallangeActivity_.intent(getActivity()).slug(challenge.getSlug()).offline(true).start();
//    }
//
//    private void setOfflineChallenges() {
//        challenges = DbHelper.getInstance(getActivity()).getAllChallenges();
//        itemsAdapter = new CustomListAdapter<>(challenges, this::challangeItemView);
//        items.setAdapter(itemsAdapter);
//        if (challenges.size() > 0) {
//            error.setText("");
//        } else {
//            error.setText("No Challenges Found!");
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
//        setOfflineChallenges();
    }
}