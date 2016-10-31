package com.example.seungwook.koreaspot.ExerciseManage;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.seungwook.koreaspot.MainActivity;
import com.example.seungwook.koreaspot.R;

/**
 * 등록한 운동시간에 대한 알림 리시버
 */

public class AlarmReceiver extends BroadcastReceiver{
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Alarm", "receiver");
        NotificationManager noti = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.noti).setTicker("예약하신 운동 시간이 되었습니다!").setWhen(System.currentTimeMillis())
                .setContentTitle("SMARTSPOT").setContentText("예약하신 운동 시간이 되었습니다!")
                .setDefaults(Notification.DEFAULT_ALL).setContentIntent(pendingIntent).setAutoCancel(true);
        noti.notify(1, builder.build());
    }
}
