package com.example.seungwook.koreaspot;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;


import com.example.seungwook.koreaspot.Main.CalendarFragment;
import com.example.seungwook.koreaspot.Main.ExerciseFragment;
import com.example.seungwook.koreaspot.Main.HomeFragment;
import com.example.seungwook.koreaspot.Main.ProfileFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

public class MainActivity extends FragmentActivity {

    private BottomBar mBottomBar;
    HomeFragment fragment1;
    CalendarFragment fragment2;
    ProfileFragment fragment3;
    ExerciseFragment fragment4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startActivity(new Intent(MainActivity.this, SplashActivity.class));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fragment1 = new HomeFragment();
        fragment2 = new CalendarFragment();
        fragment3 = new ProfileFragment();
        fragment4 = new ExerciseFragment();

        initView();


        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment1)
                    .commit();
        }

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                FragmentManager manager = getSupportFragmentManager();
                switch (menuItemId){
                    case R.id.menu_main:
                        manager.beginTransaction().replace(R.id.container, fragment1).commit();
                        break;

                    case R.id.menu_calendar:
                        manager.beginTransaction().replace(R.id.container, fragment2).commit();
                        break;

                    case R.id.menu_profile:
                        manager.beginTransaction().replace(R.id.container, fragment3).commit();
                        break;

                    case R.id.menu_exercise:
                        manager.beginTransaction().replace(R.id.container, fragment4).commit();
                        break;
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
            }
        });

        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorFont));
        mBottomBar.mapColorForTab(1, ContextCompat.getColor(this, R.color.chart_value_2));
        mBottomBar.mapColorForTab(2, ContextCompat.getColor(this, R.color.medium_spring_green));
        mBottomBar.mapColorForTab(3, ContextCompat.getColor(this, R.color.PictonBlue));
    }


    public void initView(){
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorApp));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
