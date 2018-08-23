package com.merabreak.timer;

import android.os.CountDownTimer;

/**
 * Created by Ewebcore on 08-Feb-16.
 */
public class MBCountDownTimer extends CountDownTimer {

    private int time;

    public interface MBTimerListener {
        void onTick(long millisUntilFinished);

        void onFinish();
    }

    /**
     * @param millisInFuture    The number of millis in the future from the call
     * to {@link #start()} until the countdown is done and {@link #onFinish()}
     * is called.
     * @param countDownInterval The interval along the way to receive
     * {@link #onTick(long)} callbacks.
     */

    MBTimerListener mbTimerListener;

    public MBCountDownTimer(long millisInFuture, long countDownInterval, MBTimerListener mbTimerListener) {
        super(millisInFuture, countDownInterval);
        this.mbTimerListener = mbTimerListener;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mbTimerListener.onTick(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        mbTimerListener.onFinish();
    }
}
