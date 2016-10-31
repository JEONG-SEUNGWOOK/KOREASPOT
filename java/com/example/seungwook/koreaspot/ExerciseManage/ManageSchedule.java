package com.example.seungwook.koreaspot.ExerciseManage;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.seungwook.koreaspot.ExerciseHistory.ExerciseRecode;
import com.example.seungwook.koreaspot.ExerciseHistory.ExerciseRecodeList;
import com.example.seungwook.koreaspot.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

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
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ManageSchedule extends Activity {
    static final String TAG = "ManageSchedule";
    RecyclerView recyclerView;
    @Bind(R.id.pickDate)
    TextView pickedDate;

    String date;
    int year, month, day;
    int hour, min;
    int reps, sets;
    String exerciseName;
    String result, recodeResult="";
    String dbDate;
    String yesterday;

    FloatingActionMenu floatingActionMenu;
    FloatingActionButton reservateExerciseBtn, recommendationBtn;

    RecyclerAdapter adapter;
    ArrayList<ExerciseManageList> exerciseManageLists;
    private View positiveAction;

    AlarmNotification alarmNotification;

    /* 운동 일정 추가 다이얼로그 */
    TimePicker timePicker;
    EditText editReps;
    EditText editSet;
    Spinner exerciseList;
    Button ok;
    Button cancel;

    /* 운동 일정 자동 추가 다이얼로그 */
    TimePicker autoTimePicker;
    Spinner autoSpinner;
    Spinner autoRoutine;
    Button autoOk;
    Button autoCancel;

    private int user = 1;
    private static final int beginner = 1;
    private static final int intermediate = 2;
    private static final int superior = 3;

    private static final String kickback = "KICKBACK";
    private static final String shoulderfly = "SHOULDERFLY";
    private static final String chest = "CHEST";
    private static final String bicepscurl = "BICEPSCURL";
    private static final String squat = "SQUAT";

    public void ManagerSchedule(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_schedule);
        ButterKnife.bind(this);

        setTitle("일정관리");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setBackgroundDrawable(new ColorDrawable(0x252627));
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(0x252627);
        }
        exerciseManageLists = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        Intent intent = getIntent();
        date = intent.getStringExtra("DATE");
        yesterday = intent.getStringExtra("yesterday");
        year = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 0);
        day = intent.getIntExtra("day", 0);
        Log.d("Alarm", "MS : " + year + " " + month + " " + day);
        pickedDate.setText(date);
        String getDate = intent.getStringExtra("DBDATE");
        dbDate = getDateFommat(getDate);


        adapter = new RecyclerAdapter(this, exerciseManageLists, dbDate);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setDate(year,month,day);

        alarmNotification = new AlarmNotification(getApplicationContext());
        ExerciseTask exerciseTask = new ExerciseTask();
        exerciseTask.execute();

        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        reservateExerciseBtn = (FloatingActionButton) findViewById(R.id.menu_item1);
        recommendationBtn = (FloatingActionButton) findViewById(R.id.menu_item2);
        reservateExerciseBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked
                showAddDialog();
            }
        });
        recommendationBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu second item clicked
                choiceExercise();

            }
        });


        ShowTask showTask = new ShowTask();
        showTask.execute();

        Log.d("sss", "item count : "+adapter.getItemCount() +"/"+result);

    }

    public void choiceExercise(){
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(date)
                .titleColorRes(R.color.chart_value_2)
                .customView(R.layout.dialog_add_auto, true).build();

        autoTimePicker = (TimePicker) dialog.getCustomView().findViewById(R.id.autoTimePicker);
        autoSpinner = (Spinner) dialog.getCustomView().findViewById(R.id.autoSpinner);
        autoRoutine = (Spinner) dialog.getCustomView().findViewById(R.id.autoRoutineSpinner);
        autoOk = (Button) dialog.getCustomView().findViewById(R.id.autoDialogOk);
        autoCancel = (Button) dialog.getCustomView().findViewById(R.id.autoDialogCancel);
        autoOk.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                int result = autoSpinner.getId();
                int routine = autoRoutine.getId();
                hour = autoTimePicker.getHour();
                min = autoTimePicker.getMinute();

                exerciseScheduling(result, routine);
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "일정이 추가 되었습니다!", Toast.LENGTH_SHORT).show();
            }
        });
        autoCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void showAddDialog() {

        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(date)
                .titleColorRes(R.color.chart_value_2)
                .customView(R.layout.dialog_add_schedule, true).build();

        timePicker = (TimePicker) dialog.getCustomView().findViewById(R.id.timePicker);
        exerciseList = (Spinner) dialog.getCustomView().findViewById(R.id.spinner);
        editReps = (EditText) dialog.getCustomView().findViewById(R.id.editReps);
        editSet = (EditText) dialog.getCustomView().findViewById(R.id.editSet);
        ok = (Button) dialog.getCustomView().findViewById(R.id.dialogOk);
        cancel = (Button) dialog.getCustomView().findViewById(R.id.dialogCancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseName = exerciseList.getSelectedItem().toString();
                hour = timePicker.getHour();
                min = timePicker.getMinute();
                reps = Integer.parseInt(editReps.getText().toString());
                sets = Integer.parseInt(editSet.getText().toString());

                alarmNotification.Alarm(year, month-1, day, hour, min);

                ExerciseManageList e = new ExerciseManageList(exerciseName, hour, min, reps, sets);
                exerciseManageLists.add(e);
                adapter.pushItem(exerciseManageLists);
                adapter.notifyDataSetChanged();
                MyTask myTask = new MyTask();
                myTask.execute();

                Log.d("Alarm", "update");
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "일정이 추가 되었습니다!", Toast.LENGTH_SHORT).show();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        editReps.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                positiveAction.setEnabled(s.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editSet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                positiveAction.setEnabled(s.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);


        dialog.show();

        positiveAction.setEnabled(false); // disabled by default
    }

    public String getDateFommat(String s) {
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



    class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String body = "exerDate=" + dbDate + "&exer=" + exerciseName + "&reps=" + reps + "&sets=" + sets + "&hour=" + hour + "&min=" + min;
            try {
                URL u = new URL("http://116.34.82.172/insertManager.php");
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
            //adapter.pushItem(s);
        }
    }

    class ShowTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String body = "date=" + dbDate;
            try {
                URL u = new URL("http://116.34.82.172/getManageData.php");
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
            Log.d("sss", result + "/ "+result.length() );
            adapter.pushItem(result);

            //예약된 운동이 없을 때 추천운동 item 추가
            if(adapter.getItemCount()==0){
                ExerciseManageList e = new ExerciseManageList("SQUAT",20, 4, true);
                exerciseManageLists.add(e);
                adapter.pushItem(exerciseManageLists);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void recommendation(){
        boolean isChest = false;
        boolean isBiceps = false;
        boolean isSquat = false;
        boolean isShoulder = false;
        boolean isKickback = false;
        ExerciseManageList addExercise=null;



        Log.d("recommendation","result = "+recodeResult);
        if (recodeResult.contains("DumbbelCrossOver")) isChest = true;
        if (recodeResult.contains("Shoulderfly")) isShoulder = true;
        if (recodeResult.contains("FrontSquat")) isSquat = true;
        if (recodeResult.contains("DumbbelKickBack")) isKickback = true;
        if (recodeResult.contains("Bicepcurl")) isBiceps = true;
        Log.d("recommendation", isBiceps + " " + isChest + " " + isKickback + " " + isShoulder + " " + isSquat);
        user = 3;
        /* 초급자 */
        if (user == beginner) {
            sets = 4;

                reps = 10;
                //전날 첫 번째 루틴 완료
                if ((isChest && isBiceps) ) {
                    recommendExercise(addExercise, squat);
                }
                //전날 첫 번째 루틴 중 CHEST만 완료
                else if ((isChest && !isBiceps)) {
                    recommendExercise(addExercise, bicepscurl);
                }
                //전날 첫 번째 루틴 중 BICEPSCURL 완료
                else if ((!isChest && isBiceps)) {
                    recommendExercise(addExercise, chest);
                }
                //전날 두 번째 루틴 완료
                else if ((isSquat) ) {
                    reps = 12;
                    recommendExercise(addExercise, shoulderfly);
                }
                //전날 세 번째 루틴 완료
                else if ((isShoulder) ) {
                    recommendExercise(addExercise, chest);
                    recommendExercise(addExercise, bicepscurl);
                }
                //전날 기록 없음
                else {
                    recommendExercise(addExercise, chest);
                    recommendExercise(addExercise, bicepscurl);
                }


        }
        /* 중급자 */
        else if (user == intermediate) {
            sets = 5;

                reps = 10;
                //전날 첫 번째 루틴 완료
                if ((isChest && isBiceps) ) {
                    exerciseName = "SQUAT";
                    recommendExercise(addExercise, squat);
                }
                //전날 첫 번째 루틴 중 CHEST만 완료
                else if ((isChest && !isBiceps)) {
                    recommendExercise(addExercise, bicepscurl);
                }
                //전날 첫 번째 루틴 중 BICEPSCURL 완료
                else if ((!isChest && isBiceps)) {
                    recommendExercise(addExercise, chest);
                }
                //전날 두 번째 루틴 완료
                else if ((isSquat) ) {
                    reps = 12;
                    recommendExercise(addExercise, shoulderfly);
                }
                //전날 세 번째 루틴 완료
                else if ((isShoulder && isKickback) ) {
                    recommendExercise(addExercise, shoulderfly);
                    reps = 20;
                    recommendExercise(addExercise, kickback);
                }
                //전날 세 번째 루틴 중 SHOULDERFLY 완료
                else if ((isShoulder && !isKickback)) {
                    reps = 20;
                    recommendExercise(addExercise, kickback);
                }
                //전날 세 번째 루틴 중 KICKBACK 완료
                else if ((!isShoulder && isKickback)) {
                    reps = 12;
                    recommendExercise(addExercise, shoulderfly);
                }
                //전날 기록 없음
                else {
                    recommendExercise(addExercise, chest);
                    recommendExercise(addExercise, bicepscurl);
                }


        }
        /* 상급자 */
        else {
            sets = 6;

                reps = 10;
                //전날 첫 번째 루틴 완료
                if ((isChest && isBiceps && isKickback) ) {
                    recommendExercise(addExercise, squat);
                }
                //전날 첫 번째 루틴 중 한 개 미완료
                else if ((isChest && !isBiceps && isKickback)) {
                    recommendExercise(addExercise, bicepscurl);
                }
                //전날 첫 번째 루틴 중 한 개  미완료
                else if ((!isChest && isBiceps && isKickback)) {
                    recommendExercise(addExercise, chest);
                }
                //전날 첫 번째 루틴 중 한 개  미완료
                else if ((isChest && isBiceps && !isKickback)) {
                    reps = 20;
                    recommendExercise(addExercise, kickback);
                }
                //전날 첫 번째 루틴 중 두 개  미완료
                else if ((isChest && !isBiceps && !isKickback)) {
                    recommendExercise(addExercise, bicepscurl);
                    reps = 20;
                    recommendExercise(addExercise, kickback);
                }
                //전날 첫 번째 루틴 중 두 개   미완료
                else if ((!isChest && isBiceps && !isKickback) ){
                    recommendExercise(addExercise, chest);
                    reps = 20;
                    recommendExercise(addExercise, kickback);
                }
                //전날 첫 번째 루틴 중 두 개   미완료
                else if ((!isChest && !isBiceps && isKickback) ){
                    recommendExercise(addExercise, chest);
                    recommendExercise(addExercise, bicepscurl);
                }

                //전날 두 번째 루틴 완료
                else if ((isSquat) ) {
                    reps = 12;
                    recommendExercise(addExercise, shoulderfly);
                }

                //전날 세 번째 루틴 완료
                else if ((isShoulder && isBiceps && isKickback) ) {
                    recommendExercise(addExercise, chest);
                    recommendExercise(addExercise, bicepscurl);
                    reps = 20;
                    recommendExercise(addExercise, kickback);
                }
                //전날 세 번째 루틴 중 한개 미완료
                else if ((!isShoulder && isBiceps && isKickback) ){
                    recommendExercise(addExercise, shoulderfly);
                }
                //전날 세 번째 루틴 중 한개 미완료
                else if ((isShoulder && !isBiceps && isKickback) ){
                    recommendExercise(addExercise, bicepscurl);
                }
                //전날 세 번째 루틴 중 한개 미완료
                else if ((isShoulder && isBiceps && !isKickback) ){
                    reps = 20;
                    recommendExercise(addExercise, kickback);
                }
                //전날 세 번째 루틴 중 두개 미완료
                else if ((!isShoulder && !isBiceps && isKickback) ){
                    recommendExercise(addExercise, shoulderfly);
                    reps = 20;
                    recommendExercise(addExercise, kickback);
                }
                //전날 세 번째 루틴 중 두개 미완료
                else if ((isShoulder && !isBiceps && !isKickback)) {
                    recommendExercise(addExercise, bicepscurl);
                    reps = 20;
                    recommendExercise(addExercise, kickback);
                }
                //전날 세 번째 루틴 중 두개 미완료
                else if ((!isShoulder && isBiceps && !isKickback)) {
                    recommendExercise(addExercise, shoulderfly);
                    reps = 20;
                    recommendExercise(addExercise, kickback);
                }
                //전날 기록 없음
                else {
                    recommendExercise(addExercise, chest);
                    recommendExercise(addExercise, bicepscurl);
                    reps = 20;
                    recommendExercise(addExercise, kickback);
                }


        }
    }

    public void exerciseScheduling(int choice, int routine) {
        boolean isChest = false;
        boolean isBiceps = false;
        boolean isSquat = false;
        boolean isShoulder = false;
        boolean isKickback = false;

        ArrayList<ExerciseRecodeList> ae = new ArrayList<>();
        ExerciseRecodeList yesterdayExer = new ExerciseRecodeList("2016-06-16", "SHOULDERFLY", "00:00:10", "00:00:00", 10, 2);
        ae.add(yesterdayExer);
        yesterdayExer = new ExerciseRecodeList("2016-06-16", "BICEPSCURL", "00:00:10", "00:00:00", 10, 2);
        ae.add(yesterdayExer);
        String yesterdayExerName = yesterdayExer.getExerciseName();

        user = 3;
        ExerciseManageList addExercise = new ExerciseManageList();
        if (recodeResult.contains("DumbbelCrossOver")) isChest = true;
        if (recodeResult.contains("Shoulderfly")) isShoulder = true;
        if (recodeResult.contains("FrontSquat")) isSquat = true;
        if (recodeResult.contains("DumbbelKickBack")) isKickback = true;
        if (recodeResult.contains("Bicepcurl")) isBiceps = true;
        Log.d("ddd", isBiceps + " " + isChest + " " + isKickback + " " + isShoulder + " " + isSquat);
        exerciseManageLists.clear();
        /* 초급자 */
            if (user == beginner) {
                sets = 4;
            /* Easy Mode */
                if (choice == 0) {
                    reps = 8;
                    //전날 첫 번째 루틴 완료
                    if ((isChest && isBiceps) || routine==2){
                        doExercise(addExercise, squat);
                    }
                    //전날 두 번째 루틴 완료
                    else if ((isSquat) || routine==3) {
                        reps = 10;
                        doExercise(addExercise, shoulderfly);
                    }
                    //전날 세 번째 루틴 완료
                    else if ((isShoulder) || routine==1) {
                        doExercise(addExercise, chest);
                        doExercise(addExercise, bicepscurl);
                    }
                    //전날 첫 번째 루틴 중 CHEST만 완료
                    else if ((isChest && !isBiceps)&& routine==0) {
                        doExercise(addExercise, bicepscurl);
                    }
                    //전날 첫 번째 루틴 중 BICEPSCURL 완료
                    else if ((!isChest && isBiceps)&& routine==0) {
                        doExercise(addExercise, chest);
                    }
                    //전날 기록 없음
                    else {
                        doExercise(addExercise, chest);
                        doExercise(addExercise, bicepscurl);
                    }
                }
            /* Noraml Mode */
                else if (choice == 1) {
                    reps = 10;
                    //전날 첫 번째 루틴 완료
                    if ((isChest && isBiceps) || routine==2) {
                        doExercise(addExercise, squat);
                    }
                    //전날 첫 번째 루틴 중 CHEST만 완료
                    else if ((isChest && !isBiceps)&& routine==0) {
                        doExercise(addExercise, bicepscurl);
                    }
                    //전날 첫 번째 루틴 중 BICEPSCURL 완료
                    else if ((!isChest && isBiceps)&& routine==0) {
                        doExercise(addExercise, chest);
                    }
                    //전날 두 번째 루틴 완료
                    else if ((isSquat) || routine==3) {
                        reps = 12;
                        doExercise(addExercise, shoulderfly);
                    }
                    //전날 세 번째 루틴 완료
                    else if ((isShoulder) || routine==1) {
                        doExercise(addExercise, chest);
                        doExercise(addExercise, bicepscurl);
                    }
                    //전날 기록 없음
                    else {
                        doExercise(addExercise, chest);
                        doExercise(addExercise, bicepscurl);
                    }
                }
            /* Hard Mode */
                else {
                    reps = 12;
                    //전날 첫 번째 루틴 완료
                    if ((isChest && isBiceps) || routine==2) {
                        doExercise(addExercise, squat);
                    }
                    //전날 첫 번째 루틴 중 CHEST만 완료
                    else if ((isChest && !isBiceps) && routine==0){
                        doExercise(addExercise, bicepscurl);
                    }
                    //전날 첫 번째 루틴 중 BICEPSCURL 완료
                    else if ((!isChest && isBiceps)&& routine==0) {
                        doExercise(addExercise, chest);
                    }
                    //전날 두 번째 루틴 완료
                    else if ((isSquat) || routine==3) {
                        reps = 14;
                        doExercise(addExercise, shoulderfly);
                    }
                    //전날 세 번째 루틴 완료
                    else if ((isShoulder) || routine==1) {
                        doExercise(addExercise, chest);
                        doExercise(addExercise, bicepscurl);
                    }
                    //전날 기록 없음
                    else {
                        doExercise(addExercise, chest);
                        doExercise(addExercise, bicepscurl);
                    }
                }
            }
        /* 중급자 */
            else if (user == intermediate) {
                sets = 5;
            /* Easy Mode */
                if (choice == 0) {
                    reps = 8;
                    //전날 첫 번째 루틴 완료
                    if ((isChest && isBiceps) || routine==2) {
                        doExercise(addExercise, squat);
                    }
                    //전날 첫 번째 루틴 중 CHEST만 완료
                    else if ((isChest && !isBiceps)&& routine==0) {
                        doExercise(addExercise, bicepscurl);
                    }
                    //전날 첫 번째 루틴 중 BICEPSCURL 완료
                    else if ((!isChest && isBiceps)&& routine==0) {
                        doExercise(addExercise, chest);
                    }
                    //전날 두 번째 루틴 완료
                    else if ((isSquat) || routine==3) {
                        reps = 10;
                        doExercise(addExercise, shoulderfly);
                    }
                    //전날 세 번째 루틴 완료
                    else if ((isShoulder && isKickback) || routine==1) {
                        doExercise(addExercise, shoulderfly);
                        reps = 15;
                        doExercise(addExercise, kickback);
                    }
                    //전날 세 번째 루틴 중 SHOULDERFLY 완료
                    else if ((isShoulder && !isKickback)&& routine==0) {
                        reps = 15;
                        doExercise(addExercise, kickback);
                    }
                    //전날 세 번째 루틴 중 KICKBACK 완료
                    else if ((!isShoulder && isKickback)&& routine==0) {
                        reps = 10;
                        doExercise(addExercise, shoulderfly);
                    }
                    //전날 기록 없음
                    else {
                        doExercise(addExercise, chest);
                        doExercise(addExercise, bicepscurl);
                    }
                }
            /* Noraml Mode */
                else if (choice == 1) {
                    reps = 10;
                    //전날 첫 번째 루틴 완료
                    if ((isChest && isBiceps) || routine==2) {
                        exerciseName = "SQUAT";
                        doExercise(addExercise, squat);
                    }
                    //전날 첫 번째 루틴 중 CHEST만 완료
                    else if ((isChest && !isBiceps)&& routine==0) {
                        doExercise(addExercise, bicepscurl);
                    }
                    //전날 첫 번째 루틴 중 BICEPSCURL 완료
                    else if ((!isChest && isBiceps)&& routine==0) {
                        doExercise(addExercise, chest);
                    }
                    //전날 두 번째 루틴 완료
                    else if ((isSquat) || routine==3) {
                        reps = 12;
                        doExercise(addExercise, shoulderfly);
                    }
                    //전날 세 번째 루틴 완료
                    else if ((isShoulder && isKickback) || routine==1) {
                        doExercise(addExercise, shoulderfly);
                        reps = 20;
                        doExercise(addExercise, kickback);
                    }
                    //전날 세 번째 루틴 중 SHOULDERFLY 완료
                    else if ((isShoulder && !isKickback)&& routine==0) {
                        reps = 20;
                        doExercise(addExercise, kickback);
                    }
                    //전날 세 번째 루틴 중 KICKBACK 완료
                    else if ((!isShoulder && isKickback)&& routine==0) {
                        reps = 12;
                        doExercise(addExercise, shoulderfly);
                    }
                    //전날 기록 없음
                    else {
                        doExercise(addExercise, chest);
                        doExercise(addExercise, bicepscurl);
                    }
                }
            /* Hard Mode */
                else {
                    reps = 12;
                    //전날 첫 번째 루틴 완료
                    if ((isChest && isBiceps) || routine==2) {
                        doExercise(addExercise, squat);
                    }
                    //전날 첫 번째 루틴 중 CHEST만 완료
                    else if ((isChest && !isBiceps)&& routine==0) {
                        doExercise(addExercise, bicepscurl);
                    }
                    //전날 첫 번째 루틴 중 BICEPSCURL 완료
                    else if ((!isChest && isBiceps)&& routine==0) {
                        doExercise(addExercise, chest);
                    }
                    //전날 두 번째 루틴 완료
                    else if ((isSquat) || routine==3) {
                        reps = 14;
                        doExercise(addExercise, shoulderfly);
                    }
                    //전날 세 번째 루틴 완료
                    else if ((isShoulder && isKickback) || routine==1) {
                        doExercise(addExercise, shoulderfly);
                        reps = 25;
                        doExercise(addExercise, kickback);
                    }
                    //전날 세 번째 루틴 중 SHOULDERFLY 완료
                    else if ((isShoulder && !isKickback)&& routine==0) {
                        reps = 25;
                        doExercise(addExercise, kickback);
                    }
                    //전날 세 번째 루틴 중 KICKBACK 완료
                    else if ((!isShoulder && isKickback)&& routine==0) {
                        reps = 14;
                        doExercise(addExercise, shoulderfly);
                    }
                    //전날 기록 없음
                    else {
                        doExercise(addExercise, chest);
                        doExercise(addExercise, bicepscurl);
                    }
                }
            }
        /* 상급자 */
            else {
                sets = 6;
            /* Easy Mode */
                if (choice == 0) {
                    reps = 8;
                    //전날 첫 번째 루틴 완료
                    if ((isChest && isBiceps && isKickback) || routine==2) {
                        doExercise(addExercise, squat);
                    }
                    //전날 두 번째 루틴 완료
                    else if ((isSquat) || routine==3) {
                        reps = 10;
                        doExercise(addExercise, shoulderfly);
                    }
                    //전날 세 번째 루틴 완료
                    else if ((isShoulder && isBiceps && isKickback) || routine==1) {
                        doExercise(addExercise, chest);
                        doExercise(addExercise, bicepscurl);
                        reps = 15;
                        doExercise(addExercise, kickback);
                    }
                    //전날 첫 번째 루틴 중 한 개 미완료
                    else if ((isChest && !isBiceps && isKickback)&& routine==0) {
                        doExercise(addExercise, bicepscurl);
                    }
                    //전날 첫 번째 루틴 중 한 개  미완료
                    else if ((!isChest && isBiceps && isKickback) && routine==0){
                        doExercise(addExercise, chest);
                    }
                    //전날 첫 번째 루틴 중 한 개  미완료
                    else if ((isChest && isBiceps && !isKickback)&& routine==0) {
                        reps = 15;
                        doExercise(addExercise, kickback);
                    }
                    //전날 첫 번째 루틴 중 두 개  미완료
                    else if ((isChest && !isBiceps && !isKickback)&& routine==0) {
                        doExercise(addExercise, bicepscurl);
                        reps = 15;
                        doExercise(addExercise, kickback);
                    }
                    //전날 첫 번째 루틴 중 두 개   미완료
                    else if ((!isChest && isBiceps && !isKickback) && routine==0){
                        doExercise(addExercise, chest);
                        reps = 15;
                        doExercise(addExercise, kickback);
                    }
                    //전날 첫 번째 루틴 중 두 개   미완료
                    else if ((!isChest && !isBiceps && isKickback) && routine==0){
                        doExercise(addExercise, chest);
                        doExercise(addExercise, bicepscurl);
                    }


                    //전날 세 번째 루틴 중 한개 미완료
                    else if ((!isShoulder && isBiceps && isKickback)&& routine==0) {
                        doExercise(addExercise, shoulderfly);
                    }
                    //전날 세 번째 루틴 중 한개 미완료
                    else if ((isShoulder && !isBiceps && isKickback)&& routine==0) {
                        doExercise(addExercise, bicepscurl);
                    }
                    //전날 세 번째 루틴 중 한개 미완료
                    else if ((isShoulder && isBiceps && !isKickback)&& routine==0) {
                        reps = 15;
                        doExercise(addExercise, kickback);
                    }
                    //전날 세 번째 루틴 중 두개 미완료
                    else if ((!isShoulder && !isBiceps && isKickback) && routine==0){
                        doExercise(addExercise, shoulderfly);
                        reps = 15;
                        doExercise(addExercise, kickback);
                    }
                    //전날 세 번째 루틴 중 두개 미완료
                    else if ((isShoulder && !isBiceps && !isKickback)&& routine==0) {
                        doExercise(addExercise, bicepscurl);
                        reps = 15;
                        doExercise(addExercise, kickback);
                    }
                    //전날 세 번째 루틴 중 두개 미완료
                    else if ((!isShoulder && isBiceps && !isKickback)&& routine==0) {
                        doExercise(addExercise, shoulderfly);
                        reps = 15;
                        doExercise(addExercise, kickback);
                    }
                    //전날 기록 없음
                    else {
                        doExercise(addExercise, chest);
                        doExercise(addExercise, bicepscurl);
                        reps = 15;
                        doExercise(addExercise, kickback);
                    }
                }
            /* Noraml Mode */
                else if (choice == 1) {
                    reps = 10;
                    //전날 첫 번째 루틴 완료
                    if ((isChest && isBiceps && isKickback) || routine==2) {
                        doExercise(addExercise, squat);
                    }
                    //전날 첫 번째 루틴 중 한 개 미완료
                    else if ((isChest && !isBiceps && isKickback)&& routine==0) {
                        doExercise(addExercise, bicepscurl);
                    }
                    //전날 첫 번째 루틴 중 한 개  미완료
                    else if ((!isChest && isBiceps && isKickback)&& routine==0) {
                        doExercise(addExercise, chest);
                    }
                    //전날 첫 번째 루틴 중 한 개  미완료
                    else if ((isChest && isBiceps && !isKickback)&& routine==0) {
                        reps = 20;
                        doExercise(addExercise, kickback);
                    }
                    //전날 첫 번째 루틴 중 두 개  미완료
                    else if ((isChest && !isBiceps && !isKickback)&& routine==0) {
                        doExercise(addExercise, bicepscurl);
                        reps = 20;
                        doExercise(addExercise, kickback);
                    }
                    //전날 첫 번째 루틴 중 두 개   미완료
                    else if ((!isChest && isBiceps && !isKickback) && routine==0){
                        doExercise(addExercise, chest);
                        reps = 20;
                        doExercise(addExercise, kickback);
                    }
                    //전날 첫 번째 루틴 중 두 개   미완료
                    else if ((!isChest && !isBiceps && isKickback) && routine==0){
                        doExercise(addExercise, chest);
                        doExercise(addExercise, bicepscurl);
                    }

                    //전날 두 번째 루틴 완료
                    else if ((isSquat) || routine==3) {
                        reps = 12;
                        doExercise(addExercise, shoulderfly);
                    }

                    //전날 세 번째 루틴 완료
                    else if ((isShoulder && isBiceps && isKickback) || routine==1) {
                        doExercise(addExercise, chest);
                        doExercise(addExercise, bicepscurl);
                        reps = 20;
                        doExercise(addExercise, kickback);
                    }
                    //전날 세 번째 루틴 중 한개 미완료
                    else if ((!isShoulder && isBiceps && isKickback) && routine==0){
                        doExercise(addExercise, shoulderfly);
                    }
                    //전날 세 번째 루틴 중 한개 미완료
                    else if ((isShoulder && !isBiceps && isKickback) && routine==0){
                        doExercise(addExercise, bicepscurl);
                    }
                    //전날 세 번째 루틴 중 한개 미완료
                    else if ((isShoulder && isBiceps && !isKickback) && routine==0){
                        reps = 20;
                        doExercise(addExercise, kickback);
                    }
                    //전날 세 번째 루틴 중 두개 미완료
                    else if ((!isShoulder && !isBiceps && isKickback) && routine==0){
                        doExercise(addExercise, shoulderfly);
                        reps = 20;
                        doExercise(addExercise, kickback);
                    }
                    //전날 세 번째 루틴 중 두개 미완료
                    else if ((isShoulder && !isBiceps && !isKickback)&& routine==0) {
                        doExercise(addExercise, bicepscurl);
                        reps = 20;
                        doExercise(addExercise, kickback);
                    }
                    //전날 세 번째 루틴 중 두개 미완료
                    else if ((!isShoulder && isBiceps && !isKickback)&& routine==0) {
                        doExercise(addExercise, shoulderfly);
                        reps = 20;
                        doExercise(addExercise, kickback);
                    }
                    //전날 기록 없음
                    else {
                        doExercise(addExercise, chest);
                        doExercise(addExercise, bicepscurl);
                        reps = 20;
                        doExercise(addExercise, kickback);
                    }
                }
            /* Hard Mode */
                else {
                    reps = 12;
                    //전날 첫 번째 루틴 완료
                    if ((isChest && isBiceps && isKickback) || routine==2) {
                        doExercise(addExercise, squat);
                    }
                    //전날 첫 번째 루틴 중 한 개 미완료
                    else if ((isChest && !isBiceps && isKickback)&& routine==0) {
                        doExercise(addExercise, bicepscurl);
                    }
                    //전날 첫 번째 루틴 중 한 개  미완료
                    else if ((!isChest && isBiceps && isKickback) && routine==0){
                        doExercise(addExercise, chest);
                    }
                    //전날 첫 번째 루틴 중 한 개  미완료
                    else if ((isChest && isBiceps && !isKickback)&& routine==0) {
                        reps = 25;
                        doExercise(addExercise, kickback);
                    }
                    //전날 첫 번째 루틴 중 두 개  미완료
                    else if ((isChest && !isBiceps && !isKickback)&& routine==0) {
                        doExercise(addExercise, bicepscurl);
                        reps = 25;
                        doExercise(addExercise, kickback);
                    }
                    //전날 첫 번째 루틴 중 두 개   미완료
                    else if ((!isChest && isBiceps && !isKickback)&& routine==0) {
                        doExercise(addExercise, chest);
                        reps = 25;
                        doExercise(addExercise, kickback);
                    }
                    //전날 첫 번째 루틴 중 두 개   미완료
                    else if ((!isChest && !isBiceps && isKickback)&& routine==0) {
                        doExercise(addExercise, chest);
                        doExercise(addExercise, bicepscurl);
                    }

                    //전날 두 번째 루틴 완료
                    else if ((isSquat) || routine==3) {
                        reps = 14;
                        doExercise(addExercise, shoulderfly);
                    }

                    //전날 세 번째 루틴 완료
                    else if ((isShoulder && isBiceps && isKickback) || routine==1) {
                        doExercise(addExercise, chest);
                        doExercise(addExercise, bicepscurl);
                        reps = 25;
                        doExercise(addExercise, kickback);
                    }
                    //전날 세 번째 루틴 중 한개 미완료
                    else if ((!isShoulder && isBiceps && isKickback)&& routine==0) {
                        doExercise(addExercise, shoulderfly);
                    }
                    //전날 세 번째 루틴 중 한개 미완료
                    else if ((isShoulder && !isBiceps && isKickback)&& routine==0) {
                        doExercise(addExercise, bicepscurl);
                    }
                    //전날 세 번째 루틴 중 한개 미완료
                    else if ((isShoulder && isBiceps && !isKickback)&& routine==0) {
                        reps = 25;
                        doExercise(addExercise, kickback);
                    }
                    //전날 세 번째 루틴 중 두개 미완료
                    else if ((!isShoulder && !isBiceps && isKickback)&& routine==0) {
                        doExercise(addExercise, shoulderfly);
                        reps = 25;
                        doExercise(addExercise, kickback);
                    }
                    //전날 세 번째 루틴 중 두개 미완료
                    else if ((isShoulder && !isBiceps && !isKickback)&& routine==0) {
                        doExercise(addExercise, bicepscurl);
                        reps = 25;
                        doExercise(addExercise, kickback);
                    }
                    //전날 세 번째 루틴 중 두개 미완료
                    else if ((!isShoulder && isBiceps && !isKickback)&& routine==0) {
                        doExercise(addExercise, shoulderfly);
                        reps = 25;
                        doExercise(addExercise, kickback);
                    }
                    //전날 기록 없음
                    else {
                        doExercise(addExercise, chest);
                        doExercise(addExercise, bicepscurl);
                        reps = 25;
                        doExercise(addExercise, kickback);
                    }
                }
            }
            adapter.notifyDataSetChanged();


    }
    public void doExercise(ExerciseManageList addE, String name){
        exerciseName = name;
        addE = new ExerciseManageList(exerciseName, hour, min, reps, sets);
        exerciseManageLists.add(addE);
        adapter.pushItem(exerciseManageLists);
        adapter.notifyDataSetChanged();
        alarmNotification.Alarm(year, month-1, day, hour, min);

        MyTask myTask =new MyTask();
        myTask.execute();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void recommendExercise(ExerciseManageList addE, String name){
        exerciseName = name;
        addE = new ExerciseManageList(exerciseName, reps, sets, true);
        exerciseManageLists.add(addE);
        adapter.pushItem(exerciseManageLists);
        adapter.notifyDataSetChanged();
        Log.d("sss", "reps : "+reps + " sets : "+sets);
    }

    class ExerciseTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            Log.d("ExerciseTask", ""+yesterday);
            String body = "e_date="+yesterday;
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
            recodeResult = s;

            recommendation();
        }
    }
}
