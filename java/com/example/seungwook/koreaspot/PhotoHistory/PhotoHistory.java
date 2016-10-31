package com.example.seungwook.koreaspot.PhotoHistory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.seungwook.koreaspot.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PhotoHistory extends Activity {
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private String date, result;
    private ArrayList<PictureID> picId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_history);

        setTitle("사진");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setBackgroundDrawable(new ColorDrawable(0x252627));
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(0x252627);
        }
        gridView = (GridView) findViewById(R.id.gridView);

        Intent intent = getIntent();
        date = intent.getStringExtra("DBDATE");
        //date = getDateFommat(getDate);
        picId = new ArrayList<>();

        GetImageTask getImageTask = new GetImageTask();
        getImageTask.execute();

        SystemClock.sleep(2000);
        Log.d("sss1",picId.size()+"");

        Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                gridAdapter = new GridViewAdapter(getApplication(), R.layout.grid_item_layout, getData());
                gridView.setAdapter(gridAdapter);
            }
        };

        handler.sendEmptyMessageDelayed(0, 500);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                //Create intent
                Intent intent = new Intent(PhotoHistory.this, DetailsActivity.class);
                //intent.putExtra("title", item.getTitle());

                //intent.putExtra("image", item.getImage());
                Bitmap bmp = item.getImage();
                bmp = scaleDownBitmap(bmp,100,getApplicationContext());

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bytes = stream.toByteArray();
                intent.putExtra("BMP",bytes);

                //Start details activity
                startActivity(intent);
            }


        });

    }
    // Prepare some dummy data for gridview
    private ArrayList<ImageItem> getData() {
        Log.d("sss id","sss ");
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        //TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < picId.size(); i++) {
            PictureID no = picId.get(i);
            Log.d("sss id","sss2 ");
            Bitmap bitmap = getImg(no.getPicID());
            imageItems.add(new ImageItem(bitmap, "Image#" + i));
        }
        return imageItems;
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

    public Bitmap getImg(String id){
        // URI접속시 connect() 오류 방지하는 코드 왜인진 모르겠음
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Bitmap img = null;
        Log.d("sss getIMG",id+"");
        try {


            URL url = new URL("http://116.34.82.172/get_image.php?pic_id="+id);


            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);    // 읽기모드 설정

            connection.connect();           // 연결


            InputStream input = connection.getInputStream();

            img = BitmapFactory.decodeStream(input);
            
            //imgView.setImageBitmap(img);
        } catch (IOException e) {
            e.printStackTrace();

        }
        return img;
    }





    class GetImageTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String body = "e_date="+date;
            try {
                URL u = new URL("http://116.34.82.172/getImg.php");
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

            inputImage(result);
            Log.d("sss",result+"");
        }
    }
    public void inputImage(String s){
        JSONObject fakeObject = null;
        try {
            fakeObject = new JSONObject(result);
            JSONArray jsonArray = fakeObject.getJSONArray("result");
            for(int i=0; i<jsonArray.length(); i++) {
                picId.add(new PictureID(result, i));
                Log.d("sss size",picId.get(i).getPicID()+"");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo=Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }
}








