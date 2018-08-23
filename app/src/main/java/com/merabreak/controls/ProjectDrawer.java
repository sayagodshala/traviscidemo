package com.merabreak.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.merabreak.R;

import org.androidannotations.annotations.EViewGroup;

@EViewGroup(R.layout.drawer)
public class ProjectDrawer extends LinearLayout {
    public ProjectDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
