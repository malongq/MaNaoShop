package com.manao.manaoshop.base.baseactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.manao.manaoshop.MaNaoAppaplication;
import com.manao.manaoshop.activity.LoginActivity;
import com.manao.manaoshop.bean.User;

/**
 * Created by Malong
 * on 18/12/8.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //登录拦截，如果需要登录则先登录，然后保存上一个页面的跳转意图，登陆后在进入要进入的页面
    public void startActivity(Intent intent, boolean isNeedLogin) {
        if (isNeedLogin) {//先判断是否需要登录
            User user = MaNaoAppaplication.getInstance().getUser();
            if (user != null) {//如果已经登录了
                super.startActivity(intent);
            } else {//如果还没有登录
                Intent login_intent = new Intent(this, LoginActivity.class);
                MaNaoAppaplication.getInstance().putIntent(intent);
                super.startActivity(login_intent);
            }
        }else {
            super.startActivity(intent);
        }
    }
}
