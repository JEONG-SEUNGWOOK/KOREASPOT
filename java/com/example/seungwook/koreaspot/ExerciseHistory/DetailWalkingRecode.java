package com.example.seungwook.koreaspot.ExerciseHistory;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.seungwook.koreaspot.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailWalkingRecode extends AppCompatActivity {

    @Bind(R.id.walkingTime) TextView walkingTimeView;
    @Bind(R.id.detailCal) TextView calView;
    @Bind(R.id.detailStep) TextView stepView;
    @Bind(R.id.detailStepTime) TextView stepTimeView;
    Intent intent;
    String date;
    int steps;
    String exerciseName;
    int exerciseTime;
    float distance;
    double cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_walking_recode);
        ButterKnife.bind(this);
        initView();



    }

    public void initView(){
        setTitle("상세 정보");

        intent = getIntent();
        date = intent.getStringExtra("date");
        exerciseName = intent.getStringExtra("exerciseName");
        steps = intent.getIntExtra("Steps", 0);

        cal = Math.round(steps*0.063);
        distance = Math.round(steps*0.000762f*100)/100.0f;
        exerciseTime = Math.round(steps/84);

        walkingTimeView.setText(distance+"");
        calView.setText(cal+"kcal");
        stepView.setText(steps+"걸음");
        stepTimeView.setText(exerciseTime+"분");
    }
}
