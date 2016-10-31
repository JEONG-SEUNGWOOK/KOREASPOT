package com.example.seungwook.koreaspot.ExerciseList;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.seungwook.koreaspot.R;

public class ShoulderFlyContent extends AppCompatActivity {

    MediaController mediaController;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_shoulder_fly);
        initLayout();
    }
    /**
     * 레이아웃 초기화
     */
    private void initLayout(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ctl.setTitle("SHOULDER FLY");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        videoView = (VideoView)findViewById(R.id.videoView);

        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setVideoPath("android.resource://" + getPackageName() + "/raw/shoulderfly");
        videoView.setMediaController(mediaController);
        videoView.start();
        //videoView.requestFocus();

    }
    private boolean isMediaPlaying(){
        return mediaController.isShowing();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();

    }
}
