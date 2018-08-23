package com.merabreak.itemViews;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merabreak.BaseListItemView;
import com.merabreak.R;
import com.merabreak.imageloader.ImageLoader;
import com.merabreak.models.Store;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Ewebcore on 05-Feb-16.
 */
@EViewGroup(R.layout.list_item_store)
public class StoreItemView extends RelativeLayout implements BaseListItemView<Store> {

    public Store item;

    private ImageLoader mImageLoader;

    private Context context;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.coins)
    TextView coins;

    @ViewById(R.id.image)
    ImageView image;

    public StoreItemView(Context context) {
        super(context);
        this.context = context;
        mImageLoader = new ImageLoader(context);
    }

    @Override
    public void bind(Store item) {
        this.item = item;
        title.setText(item.getTitle());
        coins.setText(item.getCoins());
    }
}