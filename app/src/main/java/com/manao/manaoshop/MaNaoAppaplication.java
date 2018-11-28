package com.manao.manaoshop;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.xutils.x;

/**
 * Created by Malong
 * on 18/11/14.
 */
public class MaNaoAppaplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);// 是否输出debug日志, 开启debug会影响性能
    }
}
