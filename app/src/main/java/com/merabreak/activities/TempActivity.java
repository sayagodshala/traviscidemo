package com.merabreak.activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.merabreak.R;
import com.yqritc.scalablevideoview.ScalableVideoView;

import java.io.IOException;

/**
 * Created by ewebcoremac1 on 12/07/18.
 */

public class TempActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    private ScalableVideoView adsVideoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        adsVideoView = (ScalableVideoView) findViewById(R.id.video_view);
    }

    void playVideoInVideoView(Uri videoUri)
    {
        adsVideoView.setOnCompletionListener(this);
        adsVideoView.setVisibility(View.VISIBLE);
        try {
            adsVideoView.setDataSource(this, videoUri);

            adsVideoView.requestFocus();
            adsVideoView.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }
}
