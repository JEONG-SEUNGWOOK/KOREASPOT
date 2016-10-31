package com.example.seungwook.koreaspot.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.seungwook.koreaspot.ExerciseHistory.ExerciseRecode;
import com.example.seungwook.koreaspot.PhotoHistory.PhotoHistory;
import com.example.seungwook.koreaspot.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by seungwoo on 2016. 5. 24..
 */

public class CalendarFragment extends Fragment implements OnDateSelectedListener{

    @Bind(R.id.calendarView)
    MaterialCalendarView calendarView;
    private Calendar calendar;
    private java.util.Date pickedDate;

    SimpleDateFormat putDate;

    private Button cal_button1;
    private Button cal_button2;
    private String pickerDate;
    private String pickedYesterday;
    private String DBDate;
    private int year, month, day;

    private static final int MANAGE = 0;
    private static final int RECODE = 0;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar,container, false);
        ButterKnife.bind(this, rootView);

        cal_button1 = (Button)rootView.findViewById(R.id.cal_button1);
        cal_button2 = (Button)rootView.findViewById(R.id.cal_button2);
        putDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat putTitleDate = new SimpleDateFormat("yyyy년 MM월 dd일");

        calendarView.setHeaderTextAppearance(R.style.AppTheme_Calendar_Header);
        calendarView.setWeekDayTextAppearance(R.style.AppTheme_Calendar_Week);
        calendarView.setDateTextAppearance(R.style.AppTheme_Calendar_Date);
        calendarView.setOnDateChangedListener(this) ;
        calendar = Calendar.getInstance();
        calendarView.setSelectedDate(calendar.getTime());
        pickedDate = calendar.getTime();
        pickerDate = putTitleDate.format(pickedDate);
        DBDate = putDate.format(calendar.getTime());


        /* 운동관리 */
        /*
        cal_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ManageSchedule.class);
                if (v.isClickable()) {
                    intent.putExtra("DATE", pickerDate);
                    intent.putExtra("DBDATE",DBDate);
                    intent.putExtra("yesterday", pickedYesterday);
                    intent.putExtra("year", year);
                    intent.putExtra("month",month);
                    intent.putExtra("day",day);
                    intent.putExtra("pickedDate", pickedDate);
                    Log.d("Alarm", "CF : "+year +" "+month+" "+day);
                    startActivityForResult(intent, MANAGE);
                }
            }
        });
        */
        cal_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PhotoHistory.class);
                if (v.isClickable()) {
                    intent.putExtra("DATE", pickerDate);
                    intent.putExtra("DBDATE",DBDate);
                    startActivityForResult(intent, MANAGE);
                }
            }
        });
        cal_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExerciseRecode.class);
                if (v.isClickable()) {
                    intent.putExtra("DATE", pickerDate);
                    intent.putExtra("DBDATE",DBDate);
                    startActivityForResult(intent, RECODE);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void GoToActivity(Class cls){
        Intent intent = new Intent(getContext(), cls);
        startActivity(intent);
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        year = date.getYear();
        month = date.getMonth()+1;
        day = date.getDay();

        pickerDate = date.getYear() + "년 " + month + "월 " + date.getDay()+"일";
        DBDate = date.getYear()+"-"+month+"-"+date.getDay();
        Date yDate = new Date(date.getDate().getTime() - (24*60*60*1000));
        pickedYesterday = putDate.format(yDate);
    }
}
