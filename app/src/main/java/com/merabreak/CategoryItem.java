package com.merabreak;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merabreak.models.challenge.Category;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Ewebcore on 05-Feb-16.
 */
@EViewGroup(R.layout.list_item_category)
public class CategoryItem extends RelativeLayout implements BaseListItemView<Category> {

    Category category;
    private Context context;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.description)
    TextView description;

    public CategoryItem(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void bind(Category item) {
        category = item;
        title.setText(category.getTitle());
        description.setText(category.getDescription());
    }
}