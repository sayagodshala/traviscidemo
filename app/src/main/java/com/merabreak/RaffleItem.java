package com.merabreak;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.merabreak.imageloader.ImageLoader;
import com.merabreak.models.Raffle;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.list_item_raffle)
public class RaffleItem extends RelativeLayout implements BaseListItemView<Raffle> {

    private ImageLoader mImageLoader;


    private Context context;

    private Raffle raffle;

    @ViewById(R.id.name)
    TextView name;

    @ViewById(R.id.description)
    TextView description;

    public RaffleItem(Context context) {
        super(context);
        this.context = context;
        mImageLoader = new ImageLoader(context);
    }

    @Override
    public void bind(Raffle item) {
        raffle = item;
        name.setText(raffle.getTitle());
        description.setText(raffle.getDescription());
    }
}
