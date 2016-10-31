package com.example.seungwook.koreaspot.ExerciseManage;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class AlarmNotification {
    private Context context;
    private int id =0;
    public AlarmNotification(Context context){
        this.context = context;
    }
    public void Alarm(int y, int M, int d, int h, int m){
        Log.d("Alarm", "noti");
        Log.d("Alarm", y+" "+M+" "+d+" "+h+" "+m);
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context.getApplicationContext(), AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id++, intent, 0 );

        Calendar calendar = Calendar.getInstance();
        calendar.set(y, M, d, h, m, 0);

        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
