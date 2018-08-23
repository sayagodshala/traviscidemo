package com.merabreak.itemViews;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merabreak.BaseListItemView;
import com.merabreak.FontUtils;
import com.merabreak.R;
import com.merabreak.models.Circles;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Vinay on 7/26/2016.
 */
@EViewGroup(R.layout.circle_item)
public class CircleItemView extends RelativeLayout implements BaseListItemView<Circles> {

    @ViewById(R.id.name)
    TextView name;
    private Context context;

    @AfterViews
    void init() {
    }

    public CircleItemView(final Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void bind(final Circles item) {
        FontUtils.setDefaultFont(context, this);
        name.setText(item.getName());
    }
}
