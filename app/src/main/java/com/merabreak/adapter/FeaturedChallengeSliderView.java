package com.merabreak.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.gson.Gson;
import com.merabreak.FontUtils;
import com.merabreak.R;
import com.merabreak.models.challenge.Challenge;

public class FeaturedChallengeSliderView extends BaseSliderView {
    public FeaturedChallengeSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.list_featured_item_challange, null);
        FontUtils.setDefaultFont(getContext(), v);
        Challenge challenge = new Gson().fromJson(getDescription(), Challenge.class);
        ImageView target = (ImageView) v.findViewById(R.id.image);
        ImageView download = (ImageView) v.findViewById(R.id.download);
        TextView title = (TextView) v.findViewById(R.id.title);
        if(!challenge.getContentLanguage().equalsIgnoreCase("en"))
            title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        TextView total_plays = (TextView) v.findViewById(R.id.total_plays);
        TextView challenge_category = (TextView) v.findViewById(R.id.slide_challenge_category);
        title.setText(challenge.getTitle());
        total_plays.setText(challenge.getTotalPlayed());
        challenge_category.setText(challenge.getCategoryName());

        try {
            title.setTextColor(Color.parseColor(challenge.getChallengeTitleColor()));
        }
        catch (Exception e){
            title.setTextColor(ContextCompat.getColor(getContext(),R.color.app_white));
        };

        try {
            total_plays.setTextColor(Color.parseColor(challenge.getChallengeTitleColor()));
        }
        catch (Exception e){
            total_plays.setTextColor(ContextCompat.getColor(getContext(),R.color.app_white));
        };

        try {
            challenge_category.getBackground().setColorFilter(Color.parseColor(challenge.getCategoryColor()), PorterDuff.Mode.SRC_IN);
        }
        catch (Exception e){
            challenge_category.getBackground().setColorFilter(ContextCompat.getColor(getContext(),R.color.app_background_new), PorterDuff.Mode.SRC_IN);

        };



        bindEventAndShow(v, target);
        return v;
    }
}