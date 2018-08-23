package com.merabreak;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merabreak.models.GooglePrediction;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Ewebcore on 05-Feb-16.
 */
@EViewGroup(R.layout.list_item_searched_location)
public class SearchedLocationItem extends RelativeLayout implements BaseListItemView<GooglePrediction> {

    GooglePrediction item;
    private Context context;

    @ViewById(R.id.description)
    TextView description;

    @ViewById(R.id.time)
    ImageView time;

    @ViewById(R.id.search)
    ImageView search;

    @ViewById(R.id.location)
    ImageView location;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.subtitle)
    TextView subtitle;


    public SearchedLocationItem(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void bind(GooglePrediction item) {
        this.item = item;

        if (item.getDescription().equalsIgnoreCase("use my location")) {

            location.setVisibility(View.VISIBLE);
            title.setText(item.getDescription());
            title.setTextColor(Color.parseColor("#6b6b6b"));
            subtitle.setVisibility(View.GONE);

        } else {
            if (item.getType() != null) {
                if (item.getType().equalsIgnoreCase("saved")) {
                    time.setVisibility(View.VISIBLE);
                }
            } else {
                search.setVisibility(View.VISIBLE);
            }

            String[] address = item.getDescription().split(",");
            title.setText(address[0]);
            if (address.length > 1) {
                String moreAddress = "";
                for (int i = 1; i < address.length; i++) {
                    if (i == 1)
                        moreAddress = address[i];
                    else
                        moreAddress += ", " + address[i];
                }
                subtitle.setText(moreAddress.trim());
            } else {
                subtitle.setVisibility(View.GONE);
            }
        }
    }
}