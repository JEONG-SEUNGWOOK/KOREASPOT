package com.example.seungwook.koreaspot.temp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.seungwook.koreaspot.ExerciseList.BicepcurlContent;
import com.example.seungwook.koreaspot.ExerciseList.ChestContent;
import com.example.seungwook.koreaspot.ExerciseList.DumbbellKickbackContent;
import com.example.seungwook.koreaspot.ExerciseList.ShoulderFlyContent;
import com.example.seungwook.koreaspot.ExerciseList.SquatContent;
import com.example.seungwook.koreaspot.R;

public class WeightListActivity extends AppCompatActivity {
    private ListView m_ListView;
    private ArrayAdapter<String> m_Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_list);

        setTitle("Exercise List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] exerciseList = {"BICEPCURL","CHEST","DUMBBELL KICKBACK","SHOULDER FLY","SQUAT"};



        m_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, exerciseList);

        //리스트뷰 레이아웃과 연동
        m_ListView = (ListView)findViewById(R.id.listView);

        // 리스트 뷰와 어댑터를 연결
        m_ListView.setAdapter(m_Adapter);

        //리스트 뷰 내 클릭 이벤트
        m_ListView.setOnItemClickListener(onClickListItem);

    }

    public AdapterView.OnItemClickListener onClickListItem = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id){

            if(position == 0){
                GoToActivity(BicepcurlContent.class);
            }
            if(position== 1){
                GoToActivity(ChestContent.class);
            }
            if(position == 2){
                GoToActivity(DumbbellKickbackContent.class);
            }
            if(position == 3){
                GoToActivity(ShoulderFlyContent.class);
            }
            if(position == 4){
                GoToActivity(SquatContent.class);
            }

        }
    };

    public void GoToActivity(Class cls){
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

}
