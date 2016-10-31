package com.example.seungwook.koreaspot.PhotoHistory;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seungwook.koreaspot.R;

public class DetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        setTitle("상세보기");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setBackgroundDrawable(new ColorDrawable(0x252627));
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(0x252627);
        }
        ImageView imageView = (ImageView) findViewById(R.id.image);
        Intent data = getIntent();


        byte[] bytes = data.getByteArrayExtra("BMP");
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageView.setImageBitmap(bmp);
//
//        String title = getIntent().getStringExtra("title");
//        Bitmap bitmap = getIntent().getParcelableExtra("image");
//
//        TextView titleTextView = (TextView) findViewById(R.id.title);
//        titleTextView.setText(title);


//        imageView.setImageBitmap(bitmap);
    }

}