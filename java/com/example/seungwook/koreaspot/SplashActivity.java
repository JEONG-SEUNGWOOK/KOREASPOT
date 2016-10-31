package com.example.seungwook.koreaspot;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import android.os.Handler;

public class SplashActivity extends Activity {
    ImageView imageView;
    Animation myFadeInAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView = (ImageView)findViewById(R.id.mainImage);
        myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        imageView.startAnimation(myFadeInAnimation);
        Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();    // 액티비티 종료
            }
        };

        handler.sendEmptyMessageDelayed(0, 3000);

    }
}
