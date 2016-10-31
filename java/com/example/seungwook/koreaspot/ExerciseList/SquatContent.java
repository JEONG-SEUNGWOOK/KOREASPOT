package com.example.seungwook.koreaspot.ExerciseList;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.seungwook.koreaspot.R;

public class SquatContent extends AppCompatActivity {

    MediaController mediaController;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_squat);
        initLayout();

    }
    /**
     * 레이아웃 초기화
     */
    private void initLayout(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ctl.setTitle("SQUAT");
        videoView = (VideoView)findViewById(R.id.videoView);

        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setVideoPath("android.resource://" + getPackageName() + "/raw/squat");
        videoView.setMediaController(mediaController);
        videoView.start();

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
