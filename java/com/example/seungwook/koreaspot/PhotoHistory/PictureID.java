package com.example.seungwook.koreaspot.PhotoHistory;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PictureID {
    private String picID;

    public PictureID(String s,int i){
        try {
            JSONObject fakeObject = new JSONObject(s);
            JSONArray jsonArray = fakeObject.getJSONArray("result");
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            this.picID = jsonObject.getString("pic_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getPicID(){return picID;}
}
