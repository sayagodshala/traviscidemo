package com.merabreak.controls;

import android.content.Context;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.merabreak.R;
import com.merabreak.models.challenge.Answer;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Ewebcore on 06-Feb-16.
 */
@EViewGroup(R.layout.item_answer)
public class ItemAnswer extends RelativeLayout {
    private Context context;

    @ViewById(R.id.check)
    Button check;

    Answer answer;

    public ItemAnswer(Context context) {
        super(context);
        this.context = context;
    }

    public void init(Answer answer) {
        this.answer = answer;
    }

    private void setAnswerData() {
        check.setText(answer.getTitle());
    }

    @Click(R.id.check)
    void onQuiz() {
        if (check.isSelected())
            check.setSelected(false);
        else
            check.setSelected(true);
    }


}
