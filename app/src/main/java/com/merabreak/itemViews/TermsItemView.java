package com.merabreak.itemViews;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merabreak.BaseListItemView;
import com.merabreak.FontUtils;
import com.merabreak.R;
import com.merabreak.models.Operators;
import com.merabreak.models.TermsAndConditions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ETPL-002 on 08-Jan-18.
 */
@EViewGroup(R.layout.terms_item)
public class TermsItemView extends RelativeLayout implements BaseListItemView<TermsAndConditions> {

    @ViewById(R.id.name)
    TextView name;
    private Context context;

    @AfterViews
    void init() {
    }

    public TermsItemView(final Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void bind(final TermsAndConditions item) {
        FontUtils.setDefaultFont(context, this);
        name.setText(item.getTermField());
    }
}
