package com.merabreak.itemViews;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merabreak.BaseListItemView;
import com.merabreak.R;
import com.merabreak.imageloader.ImageLoader;
import com.merabreak.models.challenge.Challenge;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.concurrent.TimeUnit;

/**
 * Created by Ewebcore on 05-Feb-16.
 */
@EViewGroup(R.layout.list_recycler_item_challange)
public class ChallengeItemView extends RelativeLayout implements BaseListItemView<Challenge> {

    public Challenge item;

    private ImageLoader mImageLoader;

    private Context context;

    @ViewById(R.id.type)
    TextView type;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.category)
    TextView category;

    @ViewById(R.id.coins_required)
    TextView coinsRequired;

    @ViewById(R.id.duration)
    TextView duration;

    @ViewById(R.id.coins)
    TextView coins;

    @ViewById(R.id.image)
    ImageView image;

    public ChallengeItemView(Context context) {
        super(context);
        this.context = context;
        mImageLoader = new ImageLoader(context);
    }

    @Override
    public void bind(Challenge item) {
        this.item = item;
        type.setText("Type");
        title.setText(item.getTitle());
        if (item.getCategory() != null && item.getCategory().size() > 0)
            category.setText(item.getCategory().get(0).getTitle());
        coinsRequired.setText(item.getCoinsRequire());
        coins.setText(item.getCoins() + " " + context.getResources().getString(R.string.points));

        if (item.getBackgroundImage() != null && item.getBackgroundImage().contains("http"))
            mImageLoader.DisplayImage(item.getBackgroundImage(), image);

        int timeLimit = item.getTimeLimit();

        if (timeLimit > 0) {
            long totalMillis = timeLimit * 1000;
            String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(totalMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(totalMillis)), TimeUnit.MILLISECONDS.toSeconds(totalMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalMillis)));
            duration.setText(hms);
        } else {
            duration.setText("0");
        }
    }
}