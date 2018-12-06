package com.manao.manaoshop;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mob.MobSDK;

import org.xutils.x;

/**
 * Created by Malong
 * on 18/11/14.
 */
public class MaNaoAppaplication extends Application {

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
    }
}
