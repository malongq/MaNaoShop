package com.manao.manaoshop.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.manao.manaoshop.R;
import com.manao.manaoshop.base.baseactivity.BaseActivity;

import org.xutils.x;

/**
 * Created by Malong
 * on 18/12/8.
 */
public class NewOrderActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_order);

        //引入xUtils
        x.view().inject(this);

    }

    //加载布局
//    @Override
//    public int initLayout() {
//        return R.layout.activity_new_order;
//    }

}
