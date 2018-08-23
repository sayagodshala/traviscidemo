package com.merabreak.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.merabreak.FontUtils;
import com.merabreak.R;

public class FeaturedChallengeSliderViewBanner extends BaseSliderView {
    public FeaturedChallengeSliderViewBanner(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.list_featured_item_challange, null);
        ImageView target = (ImageView) v.findViewById(R.id.image);
        ImageView download = (ImageView) v.findViewById(R.id.download);
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView total_plays = (TextView) v.findViewById(R.id.total_plays);
        TextView challenge_category = (TextView) v.findViewById(R.id.slide_challenge_category);
     /*   title.setText(getDescription().split("00")[0]);
        total_plays.setText(getDescription().split("00")[1]);
        challenge_category.setText(getDescription().split("00")[2]);
        title.setTextColor(Integer.parseInt(getDescription().split("00")[3]));
        challenge_category.getBackground().setColorFilter(Integer.parseInt(getDescription().split("00")[4]), PorterDuff.Mode.SRC_IN);*/
        bindEventAndShow(v, target);
        return v;
    }
}