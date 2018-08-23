package com.merabreak.activities;

import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.merabreak.BaseActivity;
import com.merabreak.R;
import com.merabreak.controls.ItemStep;
import com.merabreak.imageloader.ImageLoader;
import com.merabreak.models.challenge.Challenge;
import com.merabreak.models.challenge.Step;
import com.merabreak.timer.MBCountDownTimer;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ewebcore on 05-Feb-16.
 */
@EActivity(R.layout.challange)
public class OfflineChallengeActivity extends BaseActivity {

    Animation outAnimation;


    @Extra
    String slug;

    Challenge challange;

    List<Step> steps;
    Step step;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.countdown_timer)
    TextView countdownTimer;

    @ViewById(R.id.background)
    ImageView background;

    @ViewById(R.id.step_container)
    LinearLayout stepContainer;

    @ViewById(R.id.info_container)
    LinearLayout infoContainer;

    @ViewById(R.id.navigations)
    LinearLayout navigations;

    @ViewById(R.id.steppers)
    LinearLayout steppers;

    @ViewById(R.id.challange_title)
    TextView challangeTitle;

    @ViewById(R.id.challange_description)
    TextView challangeDescription;

    @ViewById(R.id.no_steps)
    TextView noSteps;

    @ViewById(R.id.next)
    Button next;

    @ViewById(R.id.start)
    Button start;

    @ViewById(R.id.previous)
    Button previous;

    private int stepCounter = 0;
    ItemStep itemStep;
    ImageLoader imageLoader;
    private MBCountDownTimer mbCountDownTimer;
    long millisUntilFinished = 0;

    private LayoutInflater layoutInflater;
    private int timeLimit = 0;

    private Timer breakTimer = null;
    private TimerTask breakTimerTask = null;
    private Handler breakHandler = null;
    private String coins = null;




}
