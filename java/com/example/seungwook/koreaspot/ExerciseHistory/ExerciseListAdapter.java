package com.example.seungwook.koreaspot.ExerciseHistory;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.seungwook.koreaspot.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ExerciseListAdapter extends ArrayAdapter<ExerciseRecodeList> {

    private ArrayList<ExerciseRecodeList> items;

    public ExerciseListAdapter(Context context, int textViewResourceId, ArrayList<ExerciseRecodeList> items) {
        super(context, textViewResourceId, items);
        this.items = items;
    }

    public void push_item(String s){
        JSONObject fakeObject = null;
        try {
            fakeObject = new JSONObject(s);
            JSONArray jsonArray = fakeObject.getJSONArray("result");
            for(int i=0; i<jsonArray.length(); i++)
                items.add(new ExerciseRecodeList(s, i));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        notifyDataSetChanged();
    }
    public void push_walking(String s){
        JSONObject fakeObject = null;
        try {
            fakeObject = new JSONObject(s);
            JSONArray jsonArray = fakeObject.getJSONArray("result");
            if(jsonArray.length() != 0)
                items.add(new ExerciseRecodeList(s));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.exercise_recode_list, null);
        }
        ExerciseRecodeList recode = items.get(position);
        if (recode != null) {
            TextView exerciseDate = (TextView) v.findViewById(R.id.exerciseDate);
            TextView exerciseName = (TextView) v.findViewById(R.id.exerciseName);
            TextView time = (TextView) v.findViewById(R.id.listTime);
            TextView reps = (TextView) v.findViewById(R.id.listReps);
            TextView set = (TextView) v.findViewById(R.id.listSet);
            if (exerciseDate != null){
                exerciseDate.setText(recode.getExerciseDate());
            }
            if(exerciseName != null){
                exerciseName.setText(recode.getExerciseName());
            }
            if(exerciseName.getText().toString().equals("walking")){
                int step = recode.getSteps();
                reps.setText("Steps : "+step);
                int stepTime = step/84;
                time.setText(stepTime + "분");
            }
            else{
                String exerTime = recode.getExerciseTime();
                time.setText(exerTime+"");
                reps.setText("Reps : "+recode.getReps());
                set.setText("Set : "+recode.getSet());
            }
        }
        return v;
    }

}
