package com.example.seungwook.koreaspot.ExerciseHistory;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.seungwook.koreaspot.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailExerciseRecode extends Activity {

    @Bind(R.id.detailExerciseName) TextView detailExerciseNameView;
    @Bind(R.id.detailDate) TextView detailDate;
    @Bind(R.id.container1) TextView container1;
    @Bind(R.id.container2) TextView container2;
    @Bind(R.id.container3) TextView container3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_exercise_recode);
        ButterKnife.bind(this);

        setTitle("상세 정보");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setBackgroundDrawable(new ColorDrawable(0x252627));
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(0x252627);
        }

        Intent intent = getIntent();
        String exer = intent.getStringExtra("exerciseName");
        String exerTime = intent.getStringExtra("exerciseTime");
        String restTime = intent.getStringExtra("restTime");
        String date = intent.getStringExtra("date");
        String reps = intent.getStringExtra("reps");
        String sets = intent.getStringExtra("sets");
        String steps = intent.getStringExtra("steps");
        detailDate.setText(date);
        detailExerciseNameView.setText(exer);


    }
}
