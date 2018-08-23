package com.merabreak;

import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.merabreak.itemViews.CircleItemView_;
import com.merabreak.models.Circles;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by Vinay on 7/26/2016.
 */
@EFragment(R.layout.circle_type_dialog)
public class CircleDailog extends BaseDailogFragment {

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.list_circle)
    ListView listCircles;

    public List<Circles> getCirclesList() {
        return circlesList;
    }

    public void setCirclesList(List<Circles> circlesList) {
        this.circlesList = circlesList;
    }

    List<Circles> circlesList;

    @AfterViews
    void init() {
        circlesList = getArguments().getParcelableArrayList("MOBILE_CIRCLE");
        listCircles.setAdapter(new CustomListAdapter<>(circlesList,
                () -> CircleItemView_.build(getActivity())));
        FontUtils.setDefaultFont(getActivity(), title);
    }

    @ItemClick(R.id.list_circle)
    void onListOperatorClick(int position) {
             //Log.d("Pos", position + "");
        BusProvider.bus().post(new CircleRecievedEvent(circlesList.get(position)));
        dismiss();
    }

    @Click(R.id.terms_close_image)
    void closeTerms(){
        dismiss();
    }
}
