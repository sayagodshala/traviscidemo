package com.merabreak;

import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.merabreak.itemViews.OpratorItemView_;
import com.merabreak.itemViews.TermsItemView_;
import com.merabreak.models.Operators;
import com.merabreak.models.TermsAndConditions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by ETPL-002 on 08-Jan-18.
 */
@EFragment(R.layout.list_item_terms)
public class TermsDialog extends BaseDailogFragment{

    @ViewById(R.id.terms_title)
    TextView terms_title;

    @ViewById(R.id.list_terms)
    ListView list_terms;

    List<TermsAndConditions> termsList;

    public List<TermsAndConditions> getTermsList() {
        return termsList;
    }

    public void setTermsList(List<TermsAndConditions> termsList) {
        this.termsList = termsList;
    }


    @AfterViews
    void init() {
        termsList = getArguments().getParcelableArrayList("TERMS_CONDITIONS");
        list_terms.setAdapter(new CustomListAdapter<>(termsList,
                () -> TermsItemView_.build(getActivity())));
        FontUtils.setDefaultFont(getActivity(), terms_title);
    }

    @Click(R.id.terms_close_image)
    void closeTerms(){
        dismiss();
    }

}
