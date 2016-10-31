package com.example.seungwook.koreaspot.temp;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.txusballesteros.widgets.FitChart;
import com.txusballesteros.widgets.FitChartValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class StepCount extends AppCompatActivity  {

//    private SensorManager sensorManager;
//    private TextView count;
//    boolean activityRunning;
//
//    FitChart fitChart = null;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_step_count);
//
//        count = (TextView) findViewById(R.id.StepCount);
//
//        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        fitChart = (FitChart)findViewById(R.id.fitChart);
//    }
//
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        activityRunning = true;
//        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
//        if (countSensor != null) {
//            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
//        } else {
//            Toast.makeText(this, "Count sensor not available!", Toast.LENGTH_LONG).show();
//        }
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        activityRunning = false;
//        // if you unregister the last listener, the hardware will stop detecting step events
////        sensorManager.unregisterListener(this);
//    }
//
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        float[] values = event.values;
//        int value = -1;
//
//        if (values.length > 0)
//            value = (int) values[0];
//
//        if (activityRunning) {
//            count.setText(String.valueOf(value) + " steps today");
//            fitChart.setValue(value/100);
//        }
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//    }
}
