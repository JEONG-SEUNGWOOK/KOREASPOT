package com.example.seungwook.koreaspot.ExerciseHistory;

import android.graphics.Bitmap;
import android.text.InputType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ExerciseRecodeList{
    private String exerciseDate;
    private String exerciseName;
    private String exerciseTime;
    private String restTime;
    private int exerciseNo;
    private int reps;
    private int set;
    private int steps;
    private Bitmap img;


    public ExerciseRecodeList(String exerciseDate, String exerciseName, String exerciseTime, String restTime, int reps, int set ){
        this.exerciseDate = exerciseDate;
        this.exerciseName = exerciseName;
        this.exerciseTime = exerciseTime;
        this.restTime = restTime;
        this.reps = reps;
        this.set = set;
        this.steps = 0;
        this.exerciseNo = 0;
    }

//    public ExerciseRecodeList(String exerciseDate, int steps){
//        this.exerciseDate = exerciseDate;
//        this.exerciseName = "";
//        this.exerciseTime = "";
//        this.restTime = "";
//        this.reps = 0;
//        this.set = 0;
//        this.steps = 0;
//    }


    public ExerciseRecodeList(String s, int i){
        try {
            JSONObject fakeObject = new JSONObject(s);
            JSONArray jsonArray = fakeObject.getJSONArray("result");
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            this.exerciseDate = jsonObject.getString("e_date");
            this.exerciseName = jsonObject.getString("e_name");
            this.exerciseTime = jsonObject.getString("e_duration");
            this.restTime = jsonObject.getString("e_rest");
            this.reps = jsonObject.getInt("e_reps");
            this.set = jsonObject.getInt("e_set");
            this.steps = 0;
            this.exerciseNo = jsonObject.getInt("e_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public ExerciseRecodeList(String s){
        try {
            JSONObject fakeObject = new JSONObject(s);
            JSONArray jsonArray = fakeObject.getJSONArray("result");
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            this.exerciseDate = jsonObject.getString("date");
            this.exerciseName = "walking";
            this.exerciseTime = "";
            this.restTime = "";
            this.reps = 0;
            this.set = 0;
            this.steps = jsonObject.getInt("steps");
            this.exerciseNo = 0;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String getExerciseDate(){
        return exerciseDate;
    }
    public String getExerciseName(){
        return exerciseName;
    }
    public String getExerciseTime(){ return exerciseTime; }
    public String getRestTime() {return restTime;}
    public int getReps(){
        return reps;
    }
    public int getSet(){
        return set;
    }
    public int getSteps() { return steps;}
    public int getExerciseNo() {return exerciseNo;}
}
