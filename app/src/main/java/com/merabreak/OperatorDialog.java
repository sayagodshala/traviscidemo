package com.merabreak;

import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.merabreak.itemViews.OpratorItemView_;
import com.merabreak.models.Operators;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by Vinay on 7/26/2016.
 */
@EFragment(R.layout.operator_type_dialog)
public class OperatorDialog extends BaseDailogFragment {

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.list_operators)
    ListView listOperators;

    List<Operators> operatorsList;

    public List<Operators> getOperatorsList() {
        return operatorsList;
    }

    public void setOperatorsList(List<Operators> operatorsList) {
        this.operatorsList = operatorsList;
    }

    @AfterViews
    void init() {
        operatorsList = getArguments().getParcelableArrayList("MOBILE_OPERATOR");
        listOperators.setAdapter(new CustomListAdapter<>(operatorsList,
                () -> OpratorItemView_.build(getActivity())));
        FontUtils.setDefaultFont(getActivity(), title);
    }

    @ItemClick(R.id.list_operators)
    void onListOperatorClick(int position) {
        //Log.d("Pos", position + "");
        BusProvider.bus().post(new OpratorRecievedEvent(operatorsList.get(position)));
        dismiss();
    }

    @Click(R.id.terms_close_image)
    void closeTerms(){
        dismiss();
    }
}
