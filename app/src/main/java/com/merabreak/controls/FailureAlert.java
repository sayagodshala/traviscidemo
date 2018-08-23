package com.merabreak.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.merabreak.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.failure_alert)
public class FailureAlert extends LinearLayout {
    @ViewById(R.id.message)
    TextView message;

    public FailureAlert(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setMessage(String message) {
        this.message.setText(message);
    }

    public void setMessage(int messageResId) {
        this.message.setText(messageResId);
    }

}
