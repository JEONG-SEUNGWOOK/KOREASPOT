package com.example.seungwook.koreaspot.ExerciseManage;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.seungwook.koreaspot.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    TextView tv1, tv2;
    ImageView imageView, deleteButton;
    RelativeLayout rl;
    View view;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        view = itemView;

        tv1 = (TextView) itemView.findViewById(R.id.list_title);
        tv2 = (TextView) itemView.findViewById(R.id.list_desc);
        imageView = (ImageView) itemView.findViewById(R.id.recommendImage);
        rl = (RelativeLayout) itemView.findViewById(R.id.cardContainer);
        deleteButton = (ImageView) itemView.findViewById(R.id.delete);


    }

}