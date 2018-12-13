package com.manao.manaoshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.manao.manaoshop.Constants;
import com.manao.manaoshop.MaNaoAppaplication;
import com.manao.manaoshop.R;
import com.manao.manaoshop.activity.AddressListActivity;
import com.manao.manaoshop.activity.LoginActivity;
import com.manao.manaoshop.base.basefragment.BaseFragment;
import com.manao.manaoshop.bean.User;
import com.manao.manaoshop.weiget.CircleImageView;
import com.squareup.picasso.Picasso;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Malong
 * on 18/11/5.
 * 我的
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    @ViewInject(R.id.img_head)
    private CircleImageView mImageHead;

    @ViewInject(R.id.txt_username)
    private TextView mTxtUserName;

    @ViewInject(R.id.btn_logout)
    private Button mbtnLogout;

    @ViewInject(R.id.tv_address)
    private TextView mTv_address;

    @Override
    protected View CreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    protected void init() {
        mImageHead.setOnClickListener(this);
        mTxtUserName.setOnClickListener(this);
        mbtnLogout.setOnClickListener(this);
        mTv_address.setOnClickListener(this);
        //加载布局
        initView();
    }

    //加载布局
    private void initView() {
        User user = MaNaoAppaplication.getInstance().getUser();
        showUser(user);
    }

    //展示用户信息
    private void showUser(User user) {
        if (user != null) {
            if (!TextUtils.isEmpty(user.getLogo_url())) {
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
    private void showHeadImage(String url) {
        Picasso.with(getActivity()).load(url).into(mImageHead);
    }

    //点击事件
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.img_head:
            case R.id.txt_username:
                //从application获取用户信息
                if (MaNaoAppaplication.getInstance().getUser() == null) {
                    //todo 跳转页面第一步：如果需要有返回值或者状态携带回来的话，需用 startActivityForResult 其中 REQUEST_CODE >= 0 才行
                    // TODO: 18/12/7  第一个参数：一个Intent对象，用于携带将跳转至下一个界面中使用的数据，使用putExtra(A,B)方法，此处存储的数据类型特别多，基本类型全部支持。
                    // TODO: 18/12/7  第二个参数：如果> = 0,当Activity结束时requestCode将归还在onActivityResult()中。以便确定返回的数据是从哪个Activity中返回，用来标识目标activity。
                    // TODO: 18/12/7  与下面的resultCode功能一致，感觉Android就是为了保证数据的严格一致性特地设置了两把锁，来保证数据的发送，目的地的严格一致性。
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivityForResult(intent, Constants.REQUEST_CODE);
                }
                break;
            case R.id.btn_logout:
                //退出登录清空信息，并更新页面
                MaNaoAppaplication.getInstance().clearUser();
                showUser(null);
                break;
            case R.id.tv_address://收货地址页面
                Intent intent = new Intent(getActivity(), AddressListActivity.class);
                startActivity(intent,true);
                break;
        }
    }

    //intent回调
    //todo 跳转页面第三步：在此函数内操作
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO: 18/12/7   第一个参数：这个整数requestCode用于与startActivityForResult中的requestCode中值进行比较判断，是以便确认返回的数据是从哪个Activity返回的。
        // TODO: 18/12/7 　第二个参数：这整数resultCode是由子Activity通过其setResult()方法返回。适用于多个activity都返回数据时，来标识到底是哪一个activity返回的值。
        // TODO: 18/12/7 　第三个参数：一个Intent对象，带有返回的数据。可以通过data.getXxxExtra( );方法来获取指定数据类型的数据
        //if (resultCode == 2 && requestCode == Constants.REQUEST_CODE) {//如果回传过来的页面 resultCode 设置的不是默认 RESULT_OK 那么就要判断 resultCode
        if (requestCode == Constants.REQUEST_CODE) {
            User user = MaNaoAppaplication.getInstance().getUser();
            showUser(user);
        }
    }
}
