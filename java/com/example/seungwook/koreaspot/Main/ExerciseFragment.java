package com.example.seungwook.koreaspot.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.seungwook.koreaspot.ExerciseList.BicepcurlContent;
import com.example.seungwook.koreaspot.ExerciseList.ChestContent;
import com.example.seungwook.koreaspot.ExerciseList.DumbbellKickbackContent;
import com.example.seungwook.koreaspot.ExerciseList.ShoulderFlyContent;
import com.example.seungwook.koreaspot.ExerciseList.SquatContent;
import com.example.seungwook.koreaspot.R;


public class ExerciseFragment extends Fragment {
    public ExerciseFragment(){

    }
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View rootView = inflater.inflate(R.layout.fragment_exercise, container, false);

        String[] exerciseList = {"BICEPCURL","CHEST","DUMBBELL KICKBACK","SHOULDER FLY","SQUAT"};



        m_Adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1, exerciseList);

        //리스트뷰 레이아웃과 연동
        m_ListView = (ListView)rootView.findViewById(R.id.listView);

        // 리스트 뷰와 어댑터를 연결
        m_ListView.setAdapter(m_Adapter);

        //리스트 뷰 내 클릭 이벤트
        m_ListView.setOnItemClickListener(onClickListItem);

        return rootView;
    }
    private ListView m_ListView;
    private ArrayAdapter<String> m_Adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);






    }

    public AdapterView.OnItemClickListener onClickListItem = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id){

            if(position == 0){
                GoToActivity(getActivity(), BicepcurlContent.class);
            }
            if(position== 1){
                GoToActivity(getActivity(), ChestContent.class);
            }
            if(position == 2){
                GoToActivity(getActivity(), DumbbellKickbackContent.class);
            }
            if(position == 3){
                GoToActivity(getActivity(), ShoulderFlyContent.class);
            }
            if(position == 4){
                GoToActivity(getActivity(), SquatContent.class);
            }

        }
    };

    public void GoToActivity(Context context, Class cls){
        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }
}
