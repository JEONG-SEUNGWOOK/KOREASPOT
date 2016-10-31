package com.example.seungwook.koreaspot.ExerciseHistory;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.seungwook.koreaspot.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ExerciseRecode extends ListActivity {

    TextView exerciseDateView;
    ArrayList<ExerciseRecodeList> exerciseRecodeLists;
    ExerciseListAdapter adapter;
    ListView listView;
    String date;
    String result;
    ImageView imgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_recode);

        setTitle("이력보기");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setBackgroundDrawable(new ColorDrawable(0x252627));
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(0x252627);
        }
        exerciseDateView = (TextView)findViewById(R.id.dateView);
        Intent intent = getIntent();
        String exerciseDate = intent.getStringExtra("DATE");
        exerciseDateView.setText(exerciseDate);
        String getDate = intent.getStringExtra("DBDATE");
        date = getDateFommat(getDate);
        exerciseRecodeLists = new ArrayList<>();
        adapter = new ExerciseListAdapter(this, R.layout.exercise_recode_list, exerciseRecodeLists);

        setListAdapter(adapter);

        ExerciseTask myTask = new ExerciseTask();
        myTask.execute();

        WalkingTask walkingTask = new WalkingTask();
        walkingTask.execute();

//        PictureTask pictureTask = new PictureTask();
//        pictureTask.execute();

        listView = getListView();

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
//            {
//                Intent i;
//                if(adapter.getItem(position).getExerciseName().equals("walking")){
//                    i = new Intent(ExerciseRecode.this, DetailWalkingRecode.class);
//                    i.putExtra("date", date);
//                    i.putExtra("Steps",adapter.getItem(position).getSteps());
//                }
//                else {
//                    i = new Intent(ExerciseRecode.this, DetailExerciseRecode.class);
//                    i.putExtra("date", date);
//                    i.putExtra("exerciseName", adapter.getItem(position).getExerciseName());
//                    i.putExtra("exerciseTime", adapter.getItem(position).getExerciseTime());
//                    i.putExtra("restTime", adapter.getItem(position).getRestTime());
//                    i.putExtra("reps", adapter.getItem(position).getReps());
//                    i.putExtra("sets", adapter.getItem(position).getSet());
//                }
//                startActivity(i);
//            }
//        });

    }



    class ExerciseTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String body = "e_date="+date;
            try {
                URL u = new URL("http://116.34.82.172/getdata.php");
                HttpURLConnection huc = (HttpURLConnection) u.openConnection();
                huc.setRequestMethod("POST");
                huc.setDoInput(true);
                huc.setDoOutput(true);
                huc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                OutputStream os = huc.getOutputStream();
                os.write(body.getBytes("utf-8"));
                os.flush();
                os.close();
                BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream()), huc.getContentLength());
                String buf;
                StringBuilder sb = new StringBuilder();
                while ((buf = br.readLine()) != null) {
                    Log.d("TAG", buf);
                    sb.append(buf);
                }
                br.close();
                return sb.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            result = s;
            Log.d("sss",result+"");
            adapter.push_item(result);
        }
    }

    class WalkingTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String body = "date="+date;
            try {
                URL u = new URL("http://116.34.82.172/getWalkingData.php");
                HttpURLConnection huc = (HttpURLConnection) u.openConnection();
                huc.setRequestMethod("POST");
                huc.setDoInput(true);
                huc.setDoOutput(true);
                huc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                OutputStream os = huc.getOutputStream();
                os.write(body.getBytes("utf-8"));
                os.flush();
                os.close();
                BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream()), huc.getContentLength());
                String buf;
                StringBuilder sb = new StringBuilder();
                while ((buf = br.readLine()) != null) {
                    Log.d("TAG", buf);
                    sb.append(buf);
                }
                br.close();
                return sb.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            result = s;
            Log.d("sss",result+"");
            adapter.push_walking(result);
        }
    }


    class PictureTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String body = "e_date="+date;
            try {
                URL u = new URL("http://116.34.82.172/getPicture.php");
                HttpURLConnection huc = (HttpURLConnection) u.openConnection();
                huc.setRequestMethod("POST");
                huc.setDoInput(true);
                huc.setDoOutput(true);
                huc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                huc.setRequestProperty("Connection", "Keep-Alive");
                huc.setRequestProperty("ENCTYPE", "multipart/form-data");
                OutputStream os = huc.getOutputStream();
                os.write(body.getBytes("utf-8"));
                os.flush();
                os.close();
                BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream()), huc.getContentLength());
                String buf;
                StringBuilder sb = new StringBuilder();
                while ((buf = br.readLine()) != null) {
                    Log.d("TAG", buf);
                    sb.append(buf);
                }
                br.close();
                return sb.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            result = s;

            Log.d("sss",result+"");
            try {
                JSONObject fakeObject = new JSONObject(s);
                JSONArray jsonArray = fakeObject.getJSONArray("result");
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                byte[] img = jsonObject.getString("img").getBytes();
                Log.d("sss", img+"");
                Bitmap bitImg = byteArrayToBitmap(img);
                imgView.setImageBitmap(bitImg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public String getDateFommat(String s){
        String from = null;
        try {
            from = s;
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
            from = transFormat.format(transFormat.parse(from));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return from;
    }

    public Bitmap byteArrayToBitmap( byte[] $byteArray ) {
        Bitmap bitmap = BitmapFactory.decodeByteArray( $byteArray, 0, $byteArray.length ) ;
        return bitmap ;
    }


}
