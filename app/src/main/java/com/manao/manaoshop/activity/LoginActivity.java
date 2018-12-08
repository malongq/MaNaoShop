package com.manao.manaoshop.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.manao.manaoshop.Constants;
import com.manao.manaoshop.MaNaoAppaplication;
import com.manao.manaoshop.R;
import com.manao.manaoshop.base.baseactivity.BaseActivity;
import com.manao.manaoshop.bean.LoginRespMsg;
import com.manao.manaoshop.bean.User;
import com.manao.manaoshop.http.ApiService;
import com.manao.manaoshop.http.OkHttpHelper;
import com.manao.manaoshop.http.SpotsCallBack;
import com.manao.manaoshop.utils.DESUtil;
import com.manao.manaoshop.utils.ToastUtils;
import com.manao.manaoshop.weiget.LoginEditLayout;
import com.manao.manaoshop.weiget.MaNaoToolbar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;

import okhttp3.Response;

/**
 * Created by Malong
 * on 18/12/6.
 * 登录页面
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.manao_toobar)
    private MaNaoToolbar mToolBar;

    @ViewInject(R.id.tv_phone)
    private LoginEditLayout mTv_phone;

    @ViewInject(R.id.tv_password)
    private LoginEditLayout mTv_pwd;

    @ViewInject(R.id.btn_login)
    private Button mBtn_login;

    @ViewInject(R.id.txt_toReg)//tv_miss_pwd
    private TextView txt_toReg;

    @ViewInject(R.id.tv_miss_pwd)
    private TextView tv_miss_pwd;

    OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_login);

        x.view().inject(this);

        //加载ToolBar
        initToolBar();

        //加载布局
        initView();

    }

    //加载ToolBar
    private void initToolBar() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
    }

    //返回键
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    //加载布局
    private void initView() {

        mTv_phone.setOnClickListener(this);
        mTv_pwd.setOnClickListener(this);
        mBtn_login.setOnClickListener(this);

    }

    //点击事件
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_phone:

                break;
            case R.id.tv_password:

                break;
            case R.id.btn_login://点击登录
                String phone = mTv_phone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.show(LoginActivity.this,"请输入手机号码");
                    return;
                }

                String pwd = mTv_pwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    ToastUtils.show(LoginActivity.this,"请输入登录密码");
                    return;
                }

                HashMap<String, String> mapParmers = new HashMap<>(2);
                mapParmers.put("phone", phone);
                mapParmers.put("password", DESUtil.encode(Constants.DES_KEY, pwd));

                okHttpHelper.post(ApiService.API.LOGIN, mapParmers, new SpotsCallBack<LoginRespMsg<User>>(this) {

                    @Override
                    public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {
                        MaNaoAppaplication appaplication = MaNaoAppaplication.getInstance();
                        appaplication.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());
                        if (appaplication.getIntent() == null) {
                            //todo 跳转页面第二步：RESULT_OK 是系统的，表示成功，然后在去跳转的页面
                            // TODO: 18/12/7    在意图跳转的目的地界面调用这个方法把Activity想要返回的数据返回到主Activity，
                            // TODO: 18/12/7　　第一个参数：当Activity结束时resultCode将归还在onActivityResult()中，一般为RESULT_CANCELED , RESULT_OK该值默认为-1。
                            // TODO: 18/12/7　　第二个参数：一个Intent对象，返回给主Activity的数据。在intent对象携带了要返回的数据，使用putExtra( )方法。上面由济南大介绍。
                            //setResult(2,appaplication.getIntent());也可以这么写
                            setResult(RESULT_OK);// RESULT_OK，判断另外一个activity已经结束数据输入功能，Standard activity result:
                            finish();
                        } else {
                            appaplication.jumpToTargetActivity(LoginActivity.this);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Response response, int code, Exception e) {
                        ToastUtils.show(LoginActivity.this, "" + e.getMessage().toString().trim());
                        Log.e("登录请求出错：", e.getMessage().toString().trim());
                    }
                });
                break;
        }
    }
}
