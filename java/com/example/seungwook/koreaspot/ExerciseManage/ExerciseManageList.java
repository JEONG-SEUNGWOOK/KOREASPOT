package com.example.seungwook.koreaspot.ExerciseManage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExerciseManageList {
    private String exerciseName;
    private int hour;
    private int min;
    private int reps;
    private int sets;
    private boolean isRecommend = false;

    public ExerciseManageList(){

    }
    public ExerciseManageList(String exerciseName, int hour, int min, int reps, int sets ){
        this.exerciseName = exerciseName;
        this.hour = hour;
        this.min = min;
        this.reps = reps;
        this.sets = sets;
        this.isRecommend = false;
    }
    public ExerciseManageList(String exerciseName,int reps, int sets, boolean isRecommend ){
        this.exerciseName = exerciseName;
        this.reps = reps;
        this.sets = sets;
        this.isRecommend = isRecommend;
    }
    public ExerciseManageList(String s, int i){
        try {
            JSONObject fakeObject = new JSONObject(s);
            JSONArray jsonArray = fakeObject.getJSONArray("result");
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            this.exerciseName = jsonObject.getString("exer");
            this.reps = Integer.parseInt(jsonObject.getString("reps"));
            this.sets = Integer.parseInt(jsonObject.getString("sets"));
            this.hour = Integer.parseInt(jsonObject.getString("hour"));
            this.min = Integer.parseInt(jsonObject.getString("min"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public String getExerciseName(){return exerciseName;}
    public int getHour(){return hour;}
    public int getMin(){return min;}
    public int getReps(){return reps;}
    public int getSet(){return sets;}
    public boolean getIsRecommend(){return isRecommend;}

    public void setHour(int hour){this.hour = hour;}
    public void setMin(int min){this.min = min;}
    public void setReps(int reps){this.reps = reps;}
    public void setSets(int sets){this.sets = sets;}
    public void setIsRecommend(boolean isRecommend){this.isRecommend = isRecommend;}
}
