package com.manao.manaoshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.manao.manaoshop.R;
import com.manao.manaoshop.test.TestActivity;


/**
 * Created by Malong
 * on 18/11/5.
 * 我的
 */
public class MineFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        TextView viewById = view.findViewById(R.id.tv_test);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TestActivity.class);
                getContext().startActivity(intent);
            }
        });

        return view;
    }

}
