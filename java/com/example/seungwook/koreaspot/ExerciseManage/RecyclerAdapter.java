package com.example.seungwook.koreaspot.ExerciseManage;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
import java.util.ArrayList;


public class RecyclerAdapter extends  RecyclerView.Adapter<RecyclerViewHolder> {


    final static String TAG = "RecyclerAdapter";
    Context context;
    LayoutInflater inflater;
    ArrayList<ExerciseManageList> exerciseManageLists;
    RecyclerAdapter adapter;
    String dbDate, exer;
    String exerciseName;
    int year, month, day;
    int reps, sets;
    int hour, min;
    /* 추천 운동 일정 추가 다이얼로그 */
    TimePicker timePicker;
    EditText editReps;
    EditText editSet;
    Spinner exerciseList;
    Button ok;
    Button cancel;

    public RecyclerAdapter(Context context) {
        this.context=context;
        inflater=LayoutInflater.from(context);
    }

    public RecyclerAdapter(Context context, ArrayList<ExerciseManageList> arrayList, String date){
        this.context=context;
        this.exerciseManageLists = arrayList;
        inflater=LayoutInflater.from(context);
        this.dbDate = date;
    }
    public void setDate(int year, int month , int day){
        this.year = year;
        this.month = month;
        this.day = day;
    }
    public void pushItem(ArrayList<ExerciseManageList> items){
        this.exerciseManageLists = items;
    }
    public void pushItem(String s){
        JSONObject fakeObject = null;
        try {
            fakeObject = new JSONObject(s);
            JSONArray jsonArray = fakeObject.getJSONArray("result");
            for(int i=0; i<jsonArray.length(); i++)
                exerciseManageLists.add(new ExerciseManageList(s, i));
        }catch (JSONException e){
            e.printStackTrace();
        }
        notifyDataSetChanged();
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.schedule_items, parent, false);

        RecyclerViewHolder viewHolder=new RecyclerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        try{
            adapter = new RecyclerAdapter(context);
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    class DeleteDialog extends Dialog {
                        Button yes;
                        Button no;

                        public DeleteDialog(final Context context) {
                            super(context);

                            requestWindowFeature(Window.FEATURE_NO_TITLE);
                            setContentView(R.layout.dialog_delete);
                            yes = (Button)findViewById(R.id.dialogYes);
                            no = (Button)findViewById(R.id.dialogNo);

                            yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    exer = exerciseManageLists.get(position).getExerciseName();
                                    hour = exerciseManageLists.get(position).getHour();
                                    min = exerciseManageLists.get(position).getMin();
                                    RecyclerAdapter.DeleteTask deleteTask = new RecyclerAdapter.DeleteTask();
                                    deleteTask.execute();
                                    exerciseManageLists.remove(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "일정이 삭제 되었습니다!", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }
                            });
                            no.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dismiss();
                                }
                            });
                        }

                    }
                    DeleteDialog deleteDialog = new DeleteDialog(context);
                    deleteDialog.show();
                }
            });
            if(exerciseManageLists.get(position).getIsRecommend() == true){
                holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.favorite));
                holder.tv1.setText("추천운동 : " + exerciseManageLists.get(position).getExerciseName());
                holder.tv2.setText("예약을 원하시면 터치해주세요!");
                holder.tv1.setTextColor(context.getResources().getColor(R.color.chart_value_2));
                holder.tv2.setTextColor(context.getResources().getColor(R.color.chart_value_2));
                holder.tv1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(v.isClickable())
                            showAddRecommendDialog(position);
                    }
                });
            }
            else {
                holder.tv1.setText(exerciseManageLists.get(position).getExerciseName().toString());
                holder.tv2.setText(exerciseManageLists.get(position).getHour() + "시 "
                        + exerciseManageLists.get(position).getMin() + "분");
                holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.dumbel_white));
                holder.tv1.setTextColor(context.getResources().getColor(R.color.white));
                holder.tv2.setTextColor(context.getResources().getColor(R.color.white));
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        holder.rl.setOnClickListener(clickListener);
        holder.rl.setTag(holder);
    }
    View.OnClickListener deleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewHolder vholder = (RecyclerViewHolder) v.getTag();
            int position = vholder.getPosition();
        }
    };

    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            RecyclerViewHolder vholder = (RecyclerViewHolder) v.getTag();
            int position = vholder.getPosition();

        }
    };



    @Override
    public int getItemCount() {
        if(exerciseManageLists == null)
            return 0;
        else {
            Log.d("sss",exerciseManageLists.size()+" getItemcount");
            return exerciseManageLists.size();
        }
    }

    class DeleteTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String body = "date="+dbDate+"&exer="+exer+"&hour="+hour+"&min="+min;
            try {
                URL u = new URL("http://116.34.82.172/deleteData.php");
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
        }
    }
    public void showAddRecommendDialog(final int position) {
        Log.d(TAG, "reps: "+exerciseManageLists.get(position).getReps());


        final MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title("추천 운동 추가")
                .titleColorRes(R.color.chart_value_2)
                .customView(R.layout.dialog_add_recommend, true).build();

        timePicker = (TimePicker) dialog.getCustomView().findViewById(R.id.retimePicker);
        editReps = (EditText) dialog.getCustomView().findViewById(R.id.reeditReps);
        editSet = (EditText) dialog.getCustomView().findViewById(R.id.reeditSet);
        ok = (Button) dialog.getCustomView().findViewById(R.id.redialogOk);
        cancel = (Button) dialog.getCustomView().findViewById(R.id.redialogCancel);

        editReps.setText(""+exerciseManageLists.get(position).getReps());
        editSet.setText(""+exerciseManageLists.get(position).getSet());

        ok.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                hour = timePicker.getHour();
                min = timePicker.getMinute();
                reps = Integer.parseInt(editReps.getText().toString());
                sets = Integer.parseInt(editSet.getText().toString());


                Log.d("Alarm", year+" "+month+" "+day+" "+hour+" "+min);
                new AlarmNotification(context).Alarm(year, month-1, day, hour, min);
                exerciseName = exerciseManageLists.get(position).getExerciseName();

                exerciseManageLists.get(position).setIsRecommend(false);
                exerciseManageLists.get(position).setHour(hour);
                exerciseManageLists.get(position).setMin(min);
                MyTask myTask = new MyTask();
                myTask.execute();
                exerciseManageLists.remove(position);
                notifyDataSetChanged();
                Log.d("Alarm", "update");
                dialog.dismiss();
                Toast.makeText(context, "일정이 추가 되었습니다!", Toast.LENGTH_SHORT).show();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

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
}

