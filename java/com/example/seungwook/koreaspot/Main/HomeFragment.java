package com.example.seungwook.koreaspot.Main;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.seungwook.koreaspot.R;
import com.txusballesteros.widgets.FitChart;

import butterknife.Bind;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment implements SensorEventListener {

    @Bind(R.id.cal) TextView cal;
    @Bind(R.id.distance)  TextView dist;
    @Bind(R.id.stepTime) TextView time;
    @Bind(R.id.StepCount) TextView count;
    @Bind(R.id.goals) TextView goalsText;
    private SensorManager sensorManager;
    private Sensor countSensor;


    boolean activityRunning;
    int value;
    double calorie;
    float distance;
    double stepTime;

    int goals = 10000;
    String fGoals;

    FitChart fitChart = null;
    public HomeFragment(){

    }

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);

        try {
            goals = readSharedPreference();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fGoals = String.format("%,d", goals);

        goalsText.setText("Your goal is "+fGoals+" steps.");
        fitChart = (FitChart)rootView.findViewById(R.id.FitChart);
        fitChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.isClickable()) {
                    setGoals();
                }
            }
        });
        return rootView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onResume() {
        super.onResume();
        activityRunning = true;

        count.setText(String.valueOf(value) + " steps today");
        fitChart.setValue((value*100)/goals);

        calorie = Math.round(value*0.063);
        distance = Math.round(value*0.000762f*100)/100.0f;
        stepTime = Math.round(value/84);
        cal.setText((int)calorie+" kcal");
        dist.setText(distance+" Km");
        time.setText((int)stepTime+" 분");

        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(getContext(), "Count sensor not available!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        activityRunning = false;
        // if you unregister the last listener, the hardware will stop detecting step events
//        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        value = -1;

        if (values.length > 0)
            value = (int) values[0];

        if (activityRunning) {
            count.setText(String.valueOf(value) + " steps today");
            fitChart.setValue((value*100)/goals);

            calorie = (int)Math.round(value*0.063);
            distance = Math.round(value*0.000762f*100)/100.0f;

            stepTime = Math.round(value/84);
            cal.setText((int)calorie+" kcal");
            dist.setText(distance+" Km");
            time.setText((int)stepTime+" 분");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
     * 걸음목표수 설정 Dialog
     */
    public void setGoals(){
        new MaterialDialog.Builder(getActivity())
                .titleColorRes(R.color.PictonBlue)
                .title("목표 걸음 수를 입력해주세요.")
                .titleColorRes(R.color.colorFont)
                .inputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED|
                        InputType.TYPE_NUMBER_FLAG_DECIMAL )
                .negativeText(R.string.cancel)
                .positiveText(R.string.update)
                .input("", "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        goals = Integer.parseInt(input.toString());
                        fGoals = String.format("%,d", goals);
                        goalsText.setText("Your goal is "+fGoals+" steps.");
                        fitChart.setValue((value*100)/goals);
                        writeSharedPreference();
                        Toast.makeText(getActivity(), "업데이트 되었습니다!", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    public void writeSharedPreference(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPreference", getActivity().MODE_WORLD_READABLE| getActivity().MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("goals", goals);
        editor.commit();
    }
    public int readSharedPreference() throws Exception{
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPreference", 0);
        int value = sharedPreferences.getInt("goals",10000);
        return value;
    }
}
