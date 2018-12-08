package com.manao.manaoshop.base.basefragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.manao.manaoshop.MaNaoAppaplication;
import com.manao.manaoshop.activity.LoginActivity;
import com.manao.manaoshop.bean.User;

import org.xutils.x;

/**
 * Created by Malong
 * on 18/12/8.
 * 创建一个抽象基类  BaseFragment
 */
public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //子类必须实现的方法  抽象方法返回fragment的view
        View view = CreateView(inflater, container, savedInstanceState);

        //xutils加载布局
        x.view().inject(this, view);

        //子类可以实现也可以不实现的方法，toolbar
        initToolbar();

        //子类必须实现的方法
        init();

        return view;
    }

    protected abstract View CreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public void initToolbar() {}

    protected abstract void init();

    //登录拦截，如果需要登录则先登录，然后保存上一个页面的跳转意图，登陆后在进入要进入的页面
    public void startActivity(Intent intent, boolean isNeedLogin) {
        if (isNeedLogin) {//先判断是否需要登录
            User user = MaNaoAppaplication.getInstance().getUser();
            if (user != null) {//如果已经登录了
                super.startActivity(intent);
            } else {//如果还没有登录
                MaNaoAppaplication.getInstance().putIntent(intent);//将跳转意图保存

                Intent login_intent = new Intent(getActivity(), LoginActivity.class);
                super.startActivity(login_intent);
            }
        } else {
            super.startActivity(intent);
        }
    }

}
