package com.example.seungwook.koreaspot;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PictureList {
    int picID;
    byte[] picString;
    String picDate;

    PictureList(int picID, byte[] picString, String picDate){
        this.picID = picID;
        this.picString = picString;
        this.picDate = picDate;
    }

    PictureList(String s, int i){
        try {
            JSONObject fakeObject = new JSONObject(s);
            JSONArray jsonArray = fakeObject.getJSONArray("result");
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            this.picID = Integer.parseInt(jsonObject.getString("date"));
            this.picString = jsonObject.getString("date").getBytes();
            this.picDate = jsonObject.getString("date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getPicID(){return picID;}
    public byte[] getPicString(){return picString;}
    public String getPicDate(){return picDate;}
}
