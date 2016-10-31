package com.example.seungwook.koreaspot.ExerciseList;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.widget.NestedScrollView;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.seungwook.koreaspot.R;

public class ChestContent extends AppCompatActivity {

    MediaController mediaController;
    VideoView videoView;
    NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_chest);
        initLayout();
    }

    /**
     * 레이아웃 초기화
     */
    private void initLayout() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ctl.setTitle("CHEST");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        videoView = (VideoView)findViewById(R.id.videoView);
        nestedScrollView = (NestedScrollView)findViewById(R.id.scroll);


        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setVideoPath("android.resource://" + getPackageName() + "/raw/chest");
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
