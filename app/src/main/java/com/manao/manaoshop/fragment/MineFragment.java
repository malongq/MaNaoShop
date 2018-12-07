package com.manao.manaoshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.manao.manaoshop.Constants;
import com.manao.manaoshop.MaNaoAppaplication;
import com.manao.manaoshop.R;
import com.manao.manaoshop.activity.LoginActivity;
import com.manao.manaoshop.bean.User;
import com.manao.manaoshop.weiget.CircleImageView;
import com.squareup.picasso.Picasso;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


/**
 * Created by Malong
 * on 18/11/5.
 * 我的
 */
public class MineFragment extends Fragment implements View.OnClickListener{

    @ViewInject(R.id.img_head)
    private CircleImageView mImageHead;

    @ViewInject(R.id.txt_username)
    private TextView mTxtUserName;

    @ViewInject(R.id.btn_logout)
    private Button mbtnLogout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        x.view().inject(this, view);

        //加载布局
        initView();

        mImageHead.setOnClickListener(this);
        mTxtUserName.setOnClickListener(this);
        mbtnLogout.setOnClickListener(this);

        return view;
    }

    //加载布局
    private void initView() {
        User user = MaNaoAppaplication.getInstance().getUser();
        showUser(user);
    }

    //展示用户信息
    private void showUser(User user) {
        if (user != null) {
            if (!TextUtils.isEmpty(user.getLogo_url())){
                showHeadImage(user.getLogo_url());
            }
            mTxtUserName.setText(user.getUsername());
            mbtnLogout.setVisibility(View.VISIBLE);
        } else {
            mTxtUserName.setText(R.string.to_login);
            mImageHead.setImageResource(R.drawable.default_head);
            mbtnLogout.setVisibility(View.GONE);
        }
    }

    //展示头像
    private void showHeadImage(String url){
        Picasso.with(getActivity()).load(url).into(mImageHead);
    }

    //点击事件
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.img_head:
            case R.id.txt_username:
                //从application获取用户信息
                if(MaNaoAppaplication.getInstance().getUser() == null) {
                    //todo 跳转页面第一步：如果需要有返回值或者状态携带回来的话，需用 startActivityForResult 其中 REQUEST_CODE >= 0 才行
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivityForResult(intent, Constants.REQUEST_CODE);
                }
                break;
            case R.id.btn_logout:
                //退出登录清空信息，并更新页面
                MaNaoAppaplication.getInstance().clearUser();
                showUser(null);
                break;
        }
    }

    //intent回调
    //todo 跳转页面第三步：在此函数内操作
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE){
            User user = MaNaoAppaplication.getInstance().getUser();
            showUser(user);
        }
    }
}
