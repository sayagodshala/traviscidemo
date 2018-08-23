package com.merabreak.fragments;


import android.widget.ListView;

import com.merabreak.BaseFragment;
import com.merabreak.CustomListAdapter;
import com.merabreak.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.stores)
public class StoresFragment extends BaseFragment {


    @ViewById(R.id.items)
    ListView items;

    private CustomListAdapter itemsAdapter = null;


    @AfterViews
    void init() {


    }


}
