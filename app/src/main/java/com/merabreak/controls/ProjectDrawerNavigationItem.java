package com.merabreak.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.merabreak.NavigationItem;
import com.merabreak.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import rx.Observable;
import rx.subjects.PublishSubject;

@EViewGroup(R.layout.drawer_nav_item)
public class ProjectDrawerNavigationItem extends LinearLayout {
    @ViewById(R.id.item)
    View item;
    @ViewById(R.id.icon)
    ImageView icon;
    @ViewById(R.id.title)
    TextView title;
    private Context context;
    private PublishSubject<ProjectDrawerNavigationItem> selection = PublishSubject.create();
    private NavigationItem navigationItem;

    public ProjectDrawerNavigationItem(Context context) {
        super(context);
        this.context = context;
    }

    public ProjectDrawerNavigationItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @AfterViews
    void init() {
        item.setOnClickListener(this::navigationItemSelected);
    }

    private void navigationItemSelected(View view) {
        selection.onNext(this);
    }

    public void bind(NavigationItem navigationItem) {
        this.navigationItem = navigationItem;
        icon.setImageResource(navigationItem.getIcon());
        title.setText(navigationItem.getNavigationItemType().getTitle());
    }

    public NavigationItem getNavigationItem() {
        return navigationItem;
    }

    public Observable<ProjectDrawerNavigationItem> selected() {
        return selection.asObservable();
    }

    public View getItem() {
        return item;
    }
}
