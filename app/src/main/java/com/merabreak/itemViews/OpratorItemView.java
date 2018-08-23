package com.merabreak.itemViews;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merabreak.BaseListItemView;
import com.merabreak.FontUtils;
import com.merabreak.R;
import com.merabreak.models.Operators;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Vinay on 7/26/2016.
 */
@EViewGroup(R.layout.operator_item)
public class OpratorItemView extends RelativeLayout implements BaseListItemView<Operators> {


    @ViewById(R.id.name)
    TextView name;
    private Context context;

    @AfterViews
    void init() {
    }

    public OpratorItemView(final Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void bind(final Operators item) {
        FontUtils.setDefaultFont(context, this);
        name.setText(item.getName());
    }
}
