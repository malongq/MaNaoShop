package com.manao.manaoshop;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.manao.manaoshop.bean.User;
import com.manao.manaoshop.utils.UserLocalData;
import com.mob.MobSDK;

import org.xutils.x;

/**
 * Created by Malong
 * on 18/11/14.
 */
public class MaNaoAppaplication extends Application {

    private static MaNaoAppaplication mInstance;
    private User user;

    @Override
    public void onCreate() {
        super.onCreate();
        //Fresco加载
        Fresco.initialize(this);
        //xUtils加载
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);// 是否输出debug日志, 开启debug会影响性能
        //shareSDK
        MobSDK.init(this);
        mInstance = this;
        //获取用户信息
        initUser();
    }

    //伪单例
    public static MaNaoAppaplication getInstance() {
        return mInstance;
    }

    //一打开APP就尝试获取用户在app内的注册信息如：头像，账号，密码
    private void initUser() {
        this.user = UserLocalData.getUser(this);
    }

    //获取用户信息
    public User getUser() {
        return user;
    }

    //将用户信息保存到本地
    public void putUser(User user, String token) {
        this.user = user;
        UserLocalData.putUser(this, user);
        UserLocalData.putToken(this, token);
    }

    //清除用户信息
    public void clearUser() {
        this.user = null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);
    }

    //获取token
    public String getToken() {
        return UserLocalData.getToken(this);
    }


    /*********该方案可以使解决比如付款时没有登录，就进登录页面，登录成功后，在进入下一个要进入的页面*********/
    private Intent intent;

    public void putIntent(Intent intent) {
        this.intent = intent;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public void jumpToTargetActivity(Context context) {
        context.startActivity(intent);
        this.intent = null;
    }
    /*********该方案可以使解决比如付款时没有登录，就进登录页面，登录成功后，在进入下一个要进入的页面*********/


}
